package com.qingchao.recengine.core.plugin.merger;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.MergerPlugin;

import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-03 11:16 上午
 */
public interface IMerger<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {

    void init(MergerPlugin mergeConfig, PC pluginContext) throws Exception;

    /**
     * merge多个loader结果
     *
     * @param requestContext
     * @param input
     * @param output
     * @return
     */
    boolean merge(RC requestContext, Map<String, List<C>> input, List<C> output);
}
