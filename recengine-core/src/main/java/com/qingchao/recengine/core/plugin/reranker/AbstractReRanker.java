package com.qingchao.recengine.core.plugin.reranker;

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
 * @create 2021-03-24 5:49 下午
 */
@Slf4j
public abstract class AbstractReRanker<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractRecPlugin<PC> implements IReRanker<PC, RC, C> {

    /**
     * 插件别名
     */
    protected String name;

}
