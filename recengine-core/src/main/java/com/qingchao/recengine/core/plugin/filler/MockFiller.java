package com.qingchao.recengine.core.plugin.filler;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.FillerPlugin;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-04 10:59 上午
 */
public class MockFiller<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractFiller<RC, PC, R, C> {
    @Override
    public void init(FillerPlugin fillerConfig, PC pluginContext) {

    }

    @Override
    public boolean fill(RC requestContext, List<C> input, List<C> output) {
        //do fill
        output.addAll(input);
        return true;
    }

    public static FillerPlugin mockConfig() {
        final FillerPlugin config = new FillerPlugin();
        config.setName("MockFiller");
        config.setType("com.qingchao.recengine.core.plugin.filler.MockFiller");
        config.setParams(new HashMap<>());
        return config;
    }

    @Override
    public String getName() {
        return "mock filler";
    }
}
