package com.qingchao.recengine.core.plugin.resultselector.resultfilter;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.ResultSelectPlugin;

import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-11 10:45 上午
 */
public interface IResultFilter<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {

    void init(ResultSelectPlugin.ResultFilterPluginConfig filterConfig, PC pluginContext) throws Exception;

    /**
     * 过滤候选集
     *
     * @param requestContext
     * @param selected
     * @param output
     * @return
     */
    boolean filter(RC requestContext, C selected, List<C> output);
}
