package com.qingchao.recengine.core.bean.context;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.RecEngineConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.Set;

/**
 * 描述:单个请求的上下文
 *
 * @author kongqingchao
 * @create 2021-03-02 2:43 下午
 */
@Data
@NoArgsConstructor
public class AbstractRecRequestContext<R extends DefaultRecRequest, C extends AbstractRecCandidate> {

    private R recRequest;

    private RecEngineConfig recEngineConfig;

    private Class<C> candidateClass;

    /**
     * 正常流程还是兜底流程
     */
    private boolean normalFlow;

    /**
     * 过滤集，使用方自行加载
     */
    @Transient
    private Set<String> filterIdSet;

    public AbstractRecRequestContext(R request, RecEngineConfig recEngineConfig) {
        this.recRequest = request;
        this.recEngineConfig = recEngineConfig;
    }

}
