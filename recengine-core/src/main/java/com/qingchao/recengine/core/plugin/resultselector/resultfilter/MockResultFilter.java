package com.qingchao.recengine.core.plugin.resultselector.resultfilter;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.ResultSelectPlugin;

import java.util.List;
import java.util.Random;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-16 10:39 上午
 */
public class MockResultFilter<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> extends AbstractResultFilter<PC, RC, C> {
    @Override
    public void init(ResultSelectPlugin.ResultFilterPluginConfig filterConfig, AbstractRecPluginContext pluginContext) {

    }

    @Override
    public boolean filter(AbstractRecRequestContext requestContext, AbstractRecCandidate selected, List output) {
        final Random random = new Random(10);
        return random.nextInt(10) > 2;
    }
}
