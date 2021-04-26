package com.qingchao.recengine.core.plugin.loader;


import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.plugin.AbstractRecPlugin;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-24 3:46 下午
 */
public abstract class AbstractLoader<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext,
        C extends AbstractRecCandidate> extends AbstractRecPlugin<PC> implements ILoader<PC, RC, C> {

    /**
     * 插件别名
     */
    protected String name;


}
