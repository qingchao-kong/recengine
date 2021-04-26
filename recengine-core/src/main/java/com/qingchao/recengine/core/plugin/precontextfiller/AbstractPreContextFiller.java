package com.qingchao.recengine.core.plugin.precontextfiller;

import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.RecEngineConfig;
import com.qingchao.recengine.core.enums.StageEnum;
import com.qingchao.recengine.core.plugin.AbstractRecPlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-19 10:30 上午
 */
@Slf4j
public abstract class AbstractPreContextFiller<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext,
        R extends DefaultRecRequest> extends AbstractRecPlugin<PC> implements IPreContextFiller<PC, RC, R> {

    protected Class<RC> requestContextClass = null;

    public final RC doFill(R recRequest, RecEngineConfig recEngineConfig, boolean normalFlow) {
        long start = System.currentTimeMillis();
        RC requestContext = null;
        Throwable err = null;
        String msg = null;
        try {
            requestContext = fillContext(recRequest, recEngineConfig, normalFlow);
        } catch (Throwable throwable) {
            msg = String.format("RecEngine. AbstractPreContextFiller.doFill error, error:%s, recRequest:%s, recEngineConfig:%s, normalflow:%s",
                    throwable, recRequest, recEngineConfig, normalFlow);
            log.error(msg, throwable);
            Cat.logError(msg, throwable);
            err = throwable;
        }
        LogInfo.builder()
                .explainLog(true)
                .normalFlow(normalFlow)
                .requestId(requestContext.getRecRequest().getRequestId())
                .uid(recRequest.getUid())
                .scene(recEngineConfig.getScene())
                .startTime(start)
                .costTime(System.currentTimeMillis() - start)
                .stage(StageEnum.preContextFiller)
                .success(Objects.nonNull(requestContext))
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return requestContext;
    }

    private final RC fillContext(R recRequest, RecEngineConfig recEngineConfig, boolean normalFlow) {
        //1.requestContext初始化
        RC requestContext = null;
        try {
            requestContext = (RC) this.requestContextClass.newInstance();
        } catch (Throwable throwable) {
            final String format = String.format("RecEngine. Create request context error, error:%s, request:%s, recEngineConfig:%s", throwable, recRequest, recEngineConfig);
            Cat.logError(format, throwable);
            log.error(format, throwable);
            return null;
        }
        requestContext.setRecRequest(recRequest);
        requestContext.setRecEngineConfig(recEngineConfig);
        requestContext.setNormalFlow(normalFlow);

        //2.fill，上下文信息填充，使用方自行实现
        final boolean fill = fill((RC) requestContext);
        if (!fill) {
            final String format = String.format("RecEngine. Pre context fill false, request:%s, recEngineConfig:%s", recRequest, recEngineConfig);
            Cat.logError(format, new Exception(format));
            log.error(format);
            return null;
        }

        //3.load filter id set，加载过滤id集合，使用方自行实现
        Set<String> filterIdSet = new HashSet<>();
        final boolean loadFilterIdSet = loadFilterIdSet((RC) requestContext, filterIdSet);
        if (!loadFilterIdSet) {
            final String format = String.format("RecEngine. Pre context load filter id set false, request:%s, recEngineConfig:%s", recRequest, recEngineConfig);
            Cat.logError(format, new Exception(format));
            log.error(format);
            return null;
        }
        requestContext.setFilterIdSet(filterIdSet);
        return requestContext;
    }
}
