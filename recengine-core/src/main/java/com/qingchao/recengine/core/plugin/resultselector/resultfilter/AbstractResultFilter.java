package com.qingchao.recengine.core.plugin.resultselector.resultfilter;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.plugin.AbstractRecPlugin;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-26 10:49 上午
 */
public abstract class AbstractResultFilter<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> extends AbstractRecPlugin<PC> implements IResultFilter<PC,RC,C> {

}
