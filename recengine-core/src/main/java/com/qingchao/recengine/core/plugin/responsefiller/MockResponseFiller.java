package com.qingchao.recengine.core.plugin.responsefiller;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.bean.response.DefaultRecResponse;
import com.qingchao.recengine.core.config.ResponseFillerPlugin;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-04 11:01 上午
 */
public class MockResponseFiller<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext, R extends DefaultRecRequest,
        C extends AbstractRecCandidate, O extends DefaultRecResponse> extends AbstractResponseFiller<RC, PC, R, C, O> {
    @Override
    public void init(ResponseFillerPlugin responseFillerConfig, PC pluginContext) {

    }

    @Override
    public boolean fill(RC requestContext, List<C> input, O recResponse) {
        recResponse.setUid(requestContext.getRecRequest().getUid());
        recResponse.setResponse(input);
        return true;
    }

    public static ResponseFillerPlugin mockConfig() {
        final ResponseFillerPlugin config = new ResponseFillerPlugin();
        config.setName("MockResponseFiller");
        config.setType("com.qingchao.recengine.core.plugin.responsefiller.MockResponseFiller");
        config.setParams(new HashMap<>());
        return config;
    }
}
