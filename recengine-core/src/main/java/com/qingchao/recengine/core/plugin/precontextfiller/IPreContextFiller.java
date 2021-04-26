package com.qingchao.recengine.core.plugin.precontextfiller;

import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.PreFillerPlugin;

import java.util.Set;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-03 3:30 下午
 */
public interface IPreContextFiller<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, R extends DefaultRecRequest> {

    void init(PreFillerPlugin preContextFillerConfig, PC pluginContext, Class<RC> requestContextClass) throws Exception;

    /**
     * 对请求上下文及请求做处理
     *
     * @param requestContext
     * @return
     */
    boolean fill(RC requestContext);

    /**
     * 加载过滤集id；将需要过滤的物品的id放入filterIdSet中
     *
     * @param requestContext
     * @param filterIdSet    过滤集id
     * @return
     */
    boolean loadFilterIdSet(RC requestContext, Set<String> filterIdSet);
}
