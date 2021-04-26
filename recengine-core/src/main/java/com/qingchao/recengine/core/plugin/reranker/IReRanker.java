package com.qingchao.recengine.core.plugin.reranker;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.ReRankerPlugin;

import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-02 3:54 下午
 */
public interface IReRanker<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {

    void init(ReRankerPlugin shuffleConfig, PC pluginContext) throws Exception;

    /**
     * 重排序
     *
     * @param requestContext
     * @param input
     * @param output
     * @return
     */
    boolean rerank(RC requestContext, List<C> input, List<C> output);

    /**
     * reranker别名
     *
     * @return
     */
    String getName();

    /**
     * 获取重排规则描述
     *
     * @return
     */
    String getRegulation();
}
