package com.qingchao.recengine.core.plugin.resultselector;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.plugin.AbstractRecPlugin;
import com.qingchao.recengine.core.plugin.resultselector.resultfilter.AbstractResultFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-11 10:41 上午
 */
@Data
@Slf4j
public abstract class AbstractResultSelector<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractRecPlugin<PC> implements IResultSelector<PC, RC, C> {

    protected Integer size;
    protected List<AbstractResultFilter> mustFilters = new LinkedList<>();
    protected List<AbstractResultFilter> shouldFilters = new LinkedList<>();


}
