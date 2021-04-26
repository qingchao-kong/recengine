package com.qingchao.recengine.core.plugin.loader;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.LoaderPlugin;

import java.util.List;

/**
 * 描述:召回
 *
 * @author kongqingchao
 * @create 2021-03-02 3:01 下午
 */
public interface ILoader<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {

    void init(LoaderPlugin loaderConfig, PC pluginContext) throws Exception;

    /**
     * 加载候选集
     *
     * @param requestContext requestContext
     * @param output         候选集
     * @return 执行结果
     */
    boolean load(RC requestContext, List<C> output);

    /**
     * 返回loader的别名
     *
     * @return
     */
    String getName();

}
