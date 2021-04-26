package com.qingchao.recengine.core.plugin.filter;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.plugin.AbstractRecPlugin;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-24 4:29 下午
 */
@Slf4j
public abstract class AbstractFilter<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractRecPlugin<PC> implements IFilter<PC, RC, C> {

    /**
     * 插件别名
     */
    protected String name;

}
