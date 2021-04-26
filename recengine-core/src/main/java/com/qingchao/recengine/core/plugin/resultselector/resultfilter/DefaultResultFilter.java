package com.qingchao.recengine.core.plugin.resultselector.resultfilter;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.ResultSelectPlugin;

import java.util.List;

public class DefaultResultFilter<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> extends AbstractResultFilter<PC, RC, C> {
    @Override
    public void init(ResultSelectPlugin.ResultFilterPluginConfig filterConfig, PC pluginContext) throws Exception {
        this.pluginContext = pluginContext;
    }

    @Override
    public boolean filter(RC requestContext, C selected, List<C> output) {
        return true;
    }
}
