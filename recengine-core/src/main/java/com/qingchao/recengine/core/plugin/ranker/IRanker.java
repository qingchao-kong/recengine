package com.qingchao.recengine.core.plugin.ranker;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.RankerPlugin;

import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-02 3:52 下午
 */
public interface IRanker<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {

    void init(RankerPlugin scorerConfig, PC pluginContext) throws Exception;

    /**
     * 排序逻辑
     *
     * @param requestContext
     * @param input
     * @param output
     * @return
     */
    boolean rank(RC requestContext, List<C> input, List<C> output);
}
