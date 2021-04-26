package com.qingchao.recengine.core.plugin.filler;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.FillerPlugin;

import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-03 11:20 上午
 */
public interface IFiller<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {

    void init(FillerPlugin fillerConfig, PC pluginContext) throws Exception;

    /**
     * 填充物品信息
     *
     * @param requestContext
     * @param input
     * @param output
     * @return
     */
    boolean fill(RC requestContext, List<C> input, List<C> output);

    /**
     * filler别名
     *
     * @return
     */
    String getName();
}
