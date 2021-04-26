package com.qingchao.recengine.core.plugin.reranker;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.ReRankerPlugin;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-04 11:04 上午
 */
public class MockReRanker<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractReRanker<RC, PC, R, C> {

    private String name;

    @Override
    public void init(ReRankerPlugin shuffleConfig, PC pluginContext) {

    }

    @Override
    public boolean rerank(RC requestContext, List<C> input, List<C> output) {
        output.addAll(input);
        return true;
    }

    public static ReRankerPlugin mockConfig() {
        final ReRankerPlugin config = new ReRankerPlugin();
        config.setName("MockShuffler");
        config.setType("com.qingchao.recengine.core.plugin.reranker.MockReRanker");
        config.setParams(new HashMap<>());
        return config;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getRegulation() {
        return "mock 重排结果";
    }
}
