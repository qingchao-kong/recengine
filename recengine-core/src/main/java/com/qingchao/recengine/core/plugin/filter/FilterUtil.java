package com.qingchao.recengine.core.plugin.filter;

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
 * @create 2021-03-25 12:01 下午
 */
@Slf4j
public class FilterUtil {
    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean filter(RC requestContext, List<AbstractFilter> filters, List<C> input, List<C> output) {
        long start = System.currentTimeMillis();
        boolean succ = false;
        String msg = null;
        Throwable err = null;
        try {
            succ = doAllFilter(requestContext, filters, input, output);
        } catch (Throwable throwable) {
            err = throwable;
            msg = String.format("RecEngine. Filter stage failed, error:%s", throwable);
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
                .stage(StageEnum.filterStage)
                .success(succ)
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return succ;
    }

    public static <C extends AbstractRecCandidate> boolean doAllFilter(AbstractRecRequestContext requestContext, List<AbstractFilter> filters, List<C> input, List<C> output) {
        List<C> tmpInput = input;
        List<C> tmpOutput = new ArrayList<>();
        Iterator<AbstractFilter> iterator = filters.iterator();
        while (iterator.hasNext()) {
            AbstractFilter filter = iterator.next();
            if (doOneFilter(requestContext, filter, tmpInput, tmpOutput)) {
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

    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean doOneFilter(RC requestContext, AbstractFilter filter, List<C> input, List<C> output) {
        final long start = System.currentTimeMillis();
        boolean succ = false;
        Throwable err = null;
        String msg = null;
        List<String> idList = new ArrayList<>();
        try {
            succ = filter.filter(requestContext, input, output);
            if (succ) {
                idList = output.stream().map(AbstractRecCandidate::getId).collect(Collectors.toList());
            }
        } catch (Throwable throwable) {
            msg = String.format("RecEngine. Filter error, error:%s", throwable);
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
                .stage(StageEnum.oneFilter)
                .pluginName(filter.getName())
                .success(succ)
                .result(JSON.toJSONString(idList))
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return succ;
    }
}
