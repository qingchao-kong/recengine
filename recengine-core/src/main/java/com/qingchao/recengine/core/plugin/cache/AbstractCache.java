package com.qingchao.recengine.core.plugin.cache;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.plugin.AbstractRecPlugin;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-23 5:47 下午
 */
@Slf4j
public abstract class AbstractCache<RC extends AbstractRecRequestContext,
        PC extends AbstractRecPluginContext, C extends AbstractRecCandidate> extends AbstractRecPlugin<PC> implements ICache<PC, RC, C> {

}
