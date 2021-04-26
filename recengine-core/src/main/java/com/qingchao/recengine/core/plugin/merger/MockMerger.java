package com.qingchao.recengine.core.plugin.merger;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.MergerPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-04 10:55 上午
 */
public class MockMerger<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractMerger<RC,PC,R,C> {

    @Override
    public void init(MergerPlugin mergeConfig, PC pluginContext) {

    }

    @Override
    public boolean merge(RC requestContext, Map<String, List<C>> input, List<C> output) {
        input.forEach((k, v) -> output.addAll(v));
        return true;
    }

    public static MergerPlugin mockConfig() {
        final MergerPlugin config = new MergerPlugin();
        config.setName("MockMerger");
        config.setType("com.qingchao.recengine.core.plugin.merger.MockMerger");
        config.setSize(1000L);
        config.setParams(new HashMap<>());
        return config;
    }
}
