package com.qingchao.recengine.core.plugin.responsefiller;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.bean.response.DefaultRecResponse;
import com.qingchao.recengine.core.enums.StageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-04-25 11:06 上午
 */
@Slf4j
public class ResponseFillerUtil {

    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate, O extends DefaultRecResponse> boolean fill(RC requestContext, IResponseFiller responseFiller, List<C> input, O recResponse) {
        final long start = System.currentTimeMillis();
        boolean succ = false;
        String msg = null;
        Throwable err = null;
        try {
            succ = responseFiller.fill(requestContext, input, recResponse);
        } catch (Throwable throwable) {
            err = throwable;
            msg = String.format("RecEngine. ResponseFiller error, error:%s", throwable);
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
                .stage(StageEnum.responseFiller)
                .success(succ)
                .result(JSON.toJSONString(recResponse))
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return succ;
    }
}
