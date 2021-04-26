package com.qingchao.recengine.core.plugin.precontextfiller;

import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.PreFillerPlugin;

import java.util.HashMap;
import java.util.Set;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-19 10:46 上午
 */
public class MockPreContextFiller<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext,
        R extends DefaultRecRequest> extends AbstractPreContextFiller<PC, RC, R> {

    @Override
    public void init(PreFillerPlugin preContextFillerConfig, PC pluginContext, Class<RC> requestContextClass) {
        this.pluginContext = pluginContext;
        this.requestContextClass = requestContextClass;
    }

    @Override
    public boolean fill(RC requestContext) {
        return true;
    }

    @Override
    public boolean loadFilterIdSet(RC requestContext, Set<String> filterIdSet) {
        filterIdSet.add("3");
        filterIdSet.add("7");
        filterIdSet.add("9");
        return true;
    }

    public static PreFillerPlugin mockConfig() {
        final PreFillerPlugin config = new PreFillerPlugin();
        config.setName("preContextFiller");
        config.setType("com.qingchao.recengine.core.plugin.precontextfiller.MockPreContextFiller");
        config.setParams(new HashMap<>());
        return config;
    }
}
