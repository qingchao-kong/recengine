package com.qingchao.recengine.core.plugin.resultselector;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.ResultSelectPlugin;

import java.util.List;
import java.util.Set;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-03 11:53 上午
 */
public interface IResultSelector<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {

    void init(ResultSelectPlugin resultSelectorConfig, PC pluginContext) throws Exception;

    /**
     * 返回选中的物品
     *
     * @param requestContext
     * @param input
     * @param viewedIdSet
     * @param num
     * @return
     */
    C select(RC requestContext, List<C> input, Set<String> viewedIdSet, Integer num);

    /**
     * ResultSelector 阶段最多返回的物品数
     *
     * @return
     */
    Integer getSize();
}