package com.qingchao.recengine.core.plugin.merger;

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
 * @create 2021-03-24 4:14 下午
 */
@Slf4j
public abstract class AbstractMerger<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractRecPlugin<PC> implements IMerger<PC, RC, C> {


}
