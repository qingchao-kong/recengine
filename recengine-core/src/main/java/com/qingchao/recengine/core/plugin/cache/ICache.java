package com.qingchao.recengine.core.plugin.cache;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.CachePlugin;

import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-03 11:56 上午
 */
public interface ICache<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {

    void init(CachePlugin cacherConfig, PC pluginContext) throws Exception;

    /**
     * 将input中多余的结果缓存起来，返回给用户的结果放到output中
     *
     * @param requestContext
     * @param input
     * @param output
     * @return
     */
    boolean cacheCandidates(RC requestContext, List<C> input, List<C> output);

    /**
     * 判断是否使用缓存；如果使用缓存，从缓存池中得到数据output，并将得到的数据从缓存池中删除
     *
     * @param requestContext
     * @param output
     * @return
     */
    boolean retrieveCandidates(RC requestContext, List<C> output);

    /**
     * 缓存最小加载数量，当从缓存中加载的物品数量小于minLoadSize时，不使用缓存
     *
     * @return
     */
    int getMinLoadSize();
}
