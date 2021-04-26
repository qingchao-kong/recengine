package com.qingchao.recengine.core;

import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.bean.response.DefaultRecResponse;
import com.qingchao.recengine.core.config.RecEngineConfig;
import com.qingchao.recengine.core.enums.StageEnum;
import com.qingchao.recengine.core.plugin.RecPluginManager;
import com.qingchao.recengine.core.plugin.cache.CacheUtil;
import com.qingchao.recengine.core.plugin.filler.FillerUtil;
import com.qingchao.recengine.core.plugin.filter.FilterUtil;
import com.qingchao.recengine.core.plugin.loader.LoaderUtil;
import com.qingchao.recengine.core.plugin.merger.MergerUtil;
import com.qingchao.recengine.core.plugin.ranker.RankerUtil;
import com.qingchao.recengine.core.plugin.reranker.ReRankerUtil;
import com.qingchao.recengine.core.plugin.responsefiller.ResponseFillerUtil;
import com.qingchao.recengine.core.plugin.resultselector.ResultSelectorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 描述:整个推荐流执行逻辑
 *
 * @author kongqingchao
 * @create 2021-03-11 4:39 下午
 */
@Slf4j
@Getter
@AllArgsConstructor
public class ContextCallable<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> implements Callable<Boolean> {
    private String scene;
    private RecEngineConfig recEngineConfig;
    private DefaultRecRequest recRequest;
    private DefaultRecResponse recResponse;
    private PC pluginContext;
    private Class<RC> requestContextClass;
    private Class<C> candidateClass;
    private ThreadPoolExecutor poolExecutor;

    private boolean normalFlow;

    @Override
    public final Boolean call() {
        final long start = System.currentTimeMillis();
        boolean succ = false;
        Throwable err = null;
        String msg = null;
        try {
            succ = this.process();
        } catch (Throwable throwable) {
            final String format = String.format("RecEngine. ContextCallable execute error, error:%s, scene:%s", throwable, this.scene);
            Cat.logError(format, throwable);
            log.error(format, throwable);
            err = throwable;
            msg = format;
        }
        LogInfo.builder()
                .explainLog(true)
                .normalFlow(this.normalFlow)
                .requestId(this.recRequest.getRequestId())
                .uid(recRequest.getUid())
                .scene(this.scene)
                .startTime(start)
                .costTime(System.currentTimeMillis() - start)
                .stage(StageEnum.contextCallable)
                .success(succ)
                .throwable(err)
                .msg(msg)
                .build()
                .log();
        return succ;
    }

    public boolean process() throws Exception {
        //实例化pluginManager
        RecPluginManager pluginManager = new RecPluginManager(recEngineConfig, this.pluginContext, this.requestContextClass);

        //preContextFiller
        final RC requestContext = (RC) pluginManager.getPreContextFiller().doFill(recRequest, recEngineConfig, this.normalFlow);
        if (Objects.isNull(requestContext)) {
            return false;
        }
        requestContext.setCandidateClass(this.candidateClass);

        //check if use cache
        List<C> cacheCandidates = new ArrayList<>();
        if (recEngineConfig.getExtParameter().getCache()) {
            if (CacheUtil.retrieveCandidates(requestContext, pluginManager.getCache(), cacheCandidates)) {
                //使用缓存数据
                return doProcess(pluginManager, requestContext, recResponse, cacheCandidates);
            }
        }

        //不使用缓存的处理逻辑
        return doProcess(pluginManager, requestContext, recResponse);
    }

    /**
     * 不使用缓存的处理逻辑
     *
     * @param pluginManager
     * @param requestContext
     * @param recResponse
     * @return
     * @throws Exception
     */
    public <RC extends AbstractRecRequestContext> boolean doProcess(RecPluginManager pluginManager, RC requestContext, DefaultRecResponse recResponse) throws Exception {
        //multi loaders
        Map<String, List<C>> allLoadedCandidates = new ConcurrentHashMap<>();
        if (!LoaderUtil.multiLoad(pluginManager.getLoaders(), requestContext, allLoadedCandidates, poolExecutor)) {
            return false;
        }
        //merger
        List<C> mergedCandidates = new ArrayList();
        if (MergerUtil.merge(requestContext, pluginManager.getMerger(), allLoadedCandidates, mergedCandidates)) {
            return false;
        }
        //filler
        List<C> filledCandidates = new ArrayList();
        if (FillerUtil.fill(requestContext, pluginManager.getFillers(), mergedCandidates, filledCandidates)) {
            return false;
        }
        //filters
        List<C> filteredCandidates = new ArrayList();
        if (!FilterUtil.filter(requestContext, pluginManager.getFilters(), filledCandidates, filteredCandidates)) {
            return false;
        }
        //rank
        List<C> rankeredCandidates = new ArrayList<>();
        if (RankerUtil.rank(requestContext, pluginManager.getRanker(), filteredCandidates, rankeredCandidates)) {
            return false;
        }
        //reranker
        List<C> reRankeredCandidates = new ArrayList<>();
        if (!ReRankerUtil.reRank(requestContext, pluginManager.getReRankers(), rankeredCandidates, reRankeredCandidates)) {
            return false;
        }
        //result cache
        List<C> output = new ArrayList<>();
        if (CacheUtil.cacheCandidates(requestContext, pluginManager.getCache(), reRankeredCandidates, output)) {
            return false;
        }

        return doProcess(pluginManager, requestContext, recResponse, output);
    }

    /**
     * ResultSelect、ResponseFiller等两个阶段的处理逻辑
     *
     * @param pluginManager
     * @param requestContext
     * @param recResponse
     * @param input
     * @return
     */
    private <RC extends AbstractRecRequestContext> boolean doProcess(RecPluginManager pluginManager, RC requestContext, DefaultRecResponse recResponse, List<C> input) {
        //result select
        List<C> resultSelectedCandidates = new ArrayList<>();
        if (ResultSelectorUtil.resultSelect(requestContext, pluginManager.getResultSelector(), input, resultSelectedCandidates)) {
            return false;
        }

        //response fill
        if (ResponseFillerUtil.fill(requestContext, pluginManager.getResponseFiller(), resultSelectedCandidates, recResponse)) {
            return false;
        }

        return true;
    }

}
