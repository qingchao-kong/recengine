package com.qingchao.recengine.core.plugin.cache;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.enums.StageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-04-23 11:11 上午
 */
@Slf4j
public class CacheUtil {

    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean retrieveCandidates(RC requestContext, ICache cache, List<C> retrievedCandidates) {
        if (Objects.isNull(cache)) {
            return false;
        }

        //使用缓存
        final long start = System.currentTimeMillis();
        List<C> output = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        boolean succ = false;
        Throwable err = null;
        String msg = null;
        try {
            boolean load = cache.retrieveCandidates(requestContext, output);
            if (load) {
                //过滤掉filterIdSet中的物品
                output = output.stream().filter(c -> !requestContext.getFilterIdSet().contains(c.getId())).collect(Collectors.toList());
                //缓存数量足够，返回true，数量不够，返回false
                if (output.size() >= cache.getMinLoadSize()) {
                    retrievedCandidates.addAll(output);
                    idList = output.stream().map(AbstractRecCandidate::getId).collect(Collectors.toList());
                    succ = true;
                }
            }
        } catch (Throwable throwable) {
            msg = String.format("RecEngine. Load from cache error, error:%s", throwable);
            log.error(msg, throwable);
            Cat.logError(msg, throwable);
            err = throwable;
        }
        LogInfo.builder()
                .explainLog(true)
                .normalFlow(requestContext.isNormalFlow())
                .requestId(requestContext.getRecRequest().getRequestId())
                .uid(requestContext.getRecRequest().getUid())
                .scene(requestContext.getRecEngineConfig().getScene())
                .startTime(start)
                .costTime(System.currentTimeMillis() - start)
                .stage(StageEnum.loadFromCache)
                .success(succ)
                .result(JSON.toJSONString(idList))
                .throwable(err)
                .msg(msg)
                .build()
                .log();
        return succ;
    }

    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean cacheCandidates(RC requestContext, ICache cache, List<C> reRankeredCandidates, List<C> output) {
        if (Objects.isNull(cache)) {
            output.addAll(reRankeredCandidates);
            return true;
        }

        final long start = System.currentTimeMillis();
        boolean succ = false;
        List<String> idList = new ArrayList<>();
        String msg = null;
        Throwable err = null;
        try {
            succ = cache.cacheCandidates(requestContext, reRankeredCandidates, output);
            if (succ) {
                idList = output.stream().map(AbstractRecCandidate::getId).collect(Collectors.toList());
            }
        } catch (Throwable throwable) {
            err = throwable;
            msg = String.format("RecEngine. Cache candidates error, error:%s", throwable);
            log.error(msg, throwable);
            Cat.logError(msg, throwable);
        }
        LogInfo.builder()
                .explainLog(true)
                .normalFlow(requestContext.isNormalFlow())
                .requestId(requestContext.getRecRequest().getRequestId())
                .uid(requestContext.getRecRequest().getUid())
                .scene(requestContext.getRecEngineConfig().getScene())
                .startTime(start)
                .costTime(System.currentTimeMillis() - start)
                .stage(StageEnum.cache)
                .success(succ)
                .result(JSON.toJSONString(idList))
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return succ;
    }
}
