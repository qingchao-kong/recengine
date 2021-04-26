package com.qingchao.recengine.core.plugin.filler;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.enums.StageEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-04-23 3:24 下午
 */
@Slf4j
public class FillerUtil {

    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean fill(RC requestContext, List<AbstractFiller> fillers, List<C> input, List<C> output) {
        long start = System.currentTimeMillis();
        boolean succ = false;
        String msg = null;
        Throwable err = null;
        try {
            succ = doAllFiller(requestContext, fillers, input, output);
        } catch (Throwable throwable) {
            err = throwable;
            msg = String.format("RecEngine. Filler stage failed, error:%s", throwable);
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
                .stage(StageEnum.fillerStage)
                .success(succ)
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return succ;
    }

    public static <C extends AbstractRecCandidate> boolean doAllFiller(AbstractRecRequestContext requestContext, List<AbstractFiller> fillers, List<C> input, List<C> output) {
        List<C> tmpInput = input;
        List<C> tmpOutput = new ArrayList<>();
        Iterator<AbstractFiller> iterator = fillers.iterator();
        while (iterator.hasNext()) {
            AbstractFiller filler = iterator.next();
            if (doOneFiller(requestContext, filler, tmpInput, tmpOutput)) {
                if (iterator.hasNext()) {
                    tmpInput = tmpOutput;
                    tmpOutput = new ArrayList<>();
                }
            } else {
                return false;
            }
        }

        if (CollectionUtils.isNotEmpty(tmpOutput)) {
            output.addAll(tmpOutput);
            return true;
        } else {
            return false;
        }
    }

    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean doOneFiller(RC requestContext, AbstractFiller filler, List<C> input, List<C> output) {
        final long start = System.currentTimeMillis();
        boolean succ = false;
        Throwable err = null;
        String msg = null;
        List<String> idList = new ArrayList<>();
        try {
            succ = filler.fill(requestContext, input, output);
            if (succ) {
                idList = output.stream().map(AbstractRecCandidate::getId).collect(Collectors.toList());
            }
        } catch (Throwable throwable) {
            msg = String.format("RecEngine. Filler error, error:%s", throwable);
            err = throwable;
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
                .stage(StageEnum.oneFiller)
                .pluginName(filler.getName())
                .success(succ)
                .result(JSON.toJSONString(idList))
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return succ;
    }
}
