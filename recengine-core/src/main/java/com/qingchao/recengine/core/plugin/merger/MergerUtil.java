package com.qingchao.recengine.core.plugin.merger;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.enums.StageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-04-23 3:15 下午
 */
@Slf4j
public class MergerUtil {

    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean merge(RC requestContext, AbstractMerger merger, Map<String, List<C>> allLoadedCandidates, List<C> output) {
        //如果没有配置merger，则保留所有物品
        if (Objects.isNull(merger)) {
            for (List<C> value : allLoadedCandidates.values()) {
                output.addAll(value);
            }
            return true;
        }

        final long start = System.currentTimeMillis();
        List<String> idList = new ArrayList<>();
        boolean succ = false;
        Throwable err = null;
        String msg = null;
        try {
            succ = merger.merge(requestContext, allLoadedCandidates, output);
            if (succ) {
                idList = output.stream().map(AbstractRecCandidate::getId).collect(Collectors.toList());
            }
        } catch (Throwable throwable) {
            msg = String.format("RecEngine. Merger error, error:%s", throwable);
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
                .stage(StageEnum.merger)
                .success(succ)
                .result(JSON.toJSONString(idList))
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return succ;
    }
}
