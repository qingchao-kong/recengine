package com.qingchao.recengine.core.plugin.responsefiller;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.response.DefaultRecResponse;
import com.qingchao.recengine.core.config.ResponseFillerPlugin;

import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-03 11:51 上午
 */
public interface IResponseFiller<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate, O extends DefaultRecResponse> {

    void init(ResponseFillerPlugin responseFillerConfig, PC pluginContext) throws Exception;

    /**
     * 填充返回结果
     *
     * @param requestContext
     * @param input
     * @param recResponse
     * @return
     */
    boolean fill(RC requestContext, List<C> input, O recResponse);
}
