package com.qingchao.recengine.core.plugin.responsefiller;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.bean.response.DefaultRecResponse;
import com.qingchao.recengine.core.plugin.AbstractRecPlugin;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-25 11:05 上午
 */
@Slf4j
public abstract class AbstractResponseFiller<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext, R extends DefaultRecRequest,
        C extends AbstractRecCandidate, O extends DefaultRecResponse> extends AbstractRecPlugin<PC> implements IResponseFiller<PC, RC, C, O> {

}
