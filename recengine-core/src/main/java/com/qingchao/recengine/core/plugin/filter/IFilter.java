package com.qingchao.recengine.core.plugin.filter;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.FilterPlugin;

import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-02 3:49 下午
 */
public interface IFilter<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {

    void init(FilterPlugin filterConfig, PC pluginContext) throws Exception;

    /**
     * 过滤候选集
     *
     * @param requestContext
     * @param input
     * @param output
     * @return
     */
    boolean filter(RC requestContext, List<C> input, List<C> output);

    /**
     * filter别名
     *
     * @return
     */
    String getName();

}
