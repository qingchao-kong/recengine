package com.qingchao.recengine.core.plugin.ranker;

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
 * @create 2021-04-23 3:52 下午
 */
@Slf4j
public class RankerUtil {

    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean rank(RC requestContext, AbstractRanker ranker, List<C> input, List<C> output) {
        if (Objects.isNull(ranker)) {
            output.addAll(input);
            return true;
        }

        final long start = System.currentTimeMillis();
        boolean succ = false;
        List<String> idList = new ArrayList<>();
        String msg = null;
        Throwable err = null;
        try {
            succ = ranker.rank(requestContext, input, output);
            if (succ) {
                idList = output.stream().map(AbstractRecCandidate::getId).collect(Collectors.toList());
            }
        } catch (Throwable throwable) {
            err = throwable;
            msg = String.format("RecEngine. Ranker error, error:%s", throwable);
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
                .stage(StageEnum.ranker)
                .success(succ)
                .result(JSON.toJSONString(idList))
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return succ;
    }
}
