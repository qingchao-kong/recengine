package com.qingchao.recengine.core.plugin.filter;

import com.dianping.cat.Cat;
import com.googlecode.aviator.AviatorEvaluator;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.FilterPlugin;
import com.qingchao.recengine.core.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述: 基于表达式引擎、根据用户与物品属性的过滤
 * 表达式中如果使用到request中的字段，需要使用request.fieldName格式；如果使用到candidate中字段，需要使用candidate.fieldName格式
 * 如：request.gender!=candidate.gender
 * {
 * "name": "followRecPropertyFilter",
 * "filterType": "com.qingchao.recengine.core.plugin.filter.DefaultBoolFilter",
 * "params": {
 * "condition": "request.gender!=candidate.gender && candidate.is_freeze== 0 && condidate.content_count >=3"
 * }
 * }
 *
 * @author kongqingchao
 * @create 2021-03-04 10:58 上午
 */
@Slf4j
public class DefaultBoolFilter<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractFilter<RC, PC, R, C> {
    private String condition;
    private String name;

    @Override
    public void init(FilterPlugin filterConfig, PC pluginContext) {
        this.condition = (String) filterConfig.getParams().get("condition");
        Assert.notBlank(this.condition,"RecEngine. DefaultBoolFilter params.condition is blank!");
        this.name = filterConfig.getName();
    }

    @Override
    public boolean filter(RC requestContext, List<C> input, List<C> output) {
        if (CollectionUtils.isEmpty(input)) {
            return false;
        }

        List<C> res = input.stream()
                .filter(c -> checkCandidate((R) requestContext.getRecRequest(), c))
                .collect(Collectors.toList());
        output.addAll(res);

        return output.size() > 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 检查单个物品是否符合bool过滤条件
     * 表达式中如果使用到request中的字段，需要使用request.fieldName格式；如果使用到candidate中字段，需要使用candidate.fieldName格式
     *
     * @param request   request
     * @param candidate candidate
     * @return 是否满足条件
     */
    private boolean checkCandidate(R request, C candidate) {
        try {
            Map<String, Object> tmpMap = new HashMap<>(2);
            tmpMap.put("request", request);
            tmpMap.put("candidate", candidate);
            return (Boolean) AviatorEvaluator.execute(this.condition, tmpMap, true);
        } catch (Throwable throwable) {
            String format = String.format("RecEngine. DefaultBoolFilter filter error, request:%s, candidate:%s, error:%s", request, candidate, throwable);
            log.error(format, throwable);
            Cat.logError(format, throwable);
            return false;
        }
    }

    public static FilterPlugin mockConfig() {
        final FilterPlugin boolFilterConfig = new FilterPlugin();
        boolFilterConfig.setName("boolFilterConfig");
        boolFilterConfig.setType("com.qingchao.recengine.core.plugin.filter.DefaultBoolFilter");
        boolFilterConfig.setParams(new HashMap<String, Object>() {{
            put("condition", "candidate.id>'0'");
        }});
        return boolFilterConfig;
    }
}
