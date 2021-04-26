package com.qingchao.recengine.core.plugin.resultselector;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.ResultSelectPlugin;

import java.util.List;
import java.util.Set;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-26 10:35 上午
 */
public class DefaultResultSelector<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractResultSelector<RC, PC, R, C> {

    @Override
    public void init(ResultSelectPlugin resultSelectorConfig, PC pluginContext) throws Exception {
        this.size = resultSelectorConfig.getSize();
    }

    @Override
    public C select(RC requestContext, List<C> input, Set<String> viewedIdSet, Integer num) {
        return input.get(num);
    }

    @Override
    public Integer getSize() {
        return this.size;
    }
}
