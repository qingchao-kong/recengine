package com.qingchao.recengine.core.plugin.resultselector;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.ResultSelectPlugin;
import com.qingchao.recengine.core.plugin.resultselector.resultfilter.AbstractResultFilter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-15 3:19 下午
 */
public class MockResultSelector<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractResultSelector<RC, PC, R, C> {

    private static final Random RANDOM = new Random();

    @Override
    public void init(ResultSelectPlugin resultSelectorConfig, PC pluginContext) throws Exception {
        this.size = resultSelectorConfig.getSize();
        if (CollectionUtils.isNotEmpty(resultSelectorConfig.getFilterConfigs())) {
            for (ResultSelectPlugin.ResultFilterPluginConfig filterConfig : resultSelectorConfig.getFilterConfigs()) {
                final AbstractResultFilter filter = (AbstractResultFilter) Class.forName(filterConfig.getType()).newInstance();
                filter.init(filterConfig, pluginContext);
                if (filterConfig.isMust()) {
                    this.mustFilters.add(filter);
                } else {
                    this.shouldFilters.add(filter);
                }
            }
        }
    }

    @Override
    public C select(RC requestContext, List<C> input, Set<String> viewedIdSet, Integer num) {
        return input.get(RANDOM.nextInt(input.size()));
    }

    public static ResultSelectPlugin mockConfig() {
        final ResultSelectPlugin config = new ResultSelectPlugin();
        config.setName("MockResultSelector");
        config.setType("com.qingchao.recengine.core.plugin.resultselector.MockResultSelector");

        config.setSelectParams(new HashMap<>());
        config.setSize(10);

        final LinkedList<ResultSelectPlugin.ResultFilterPluginConfig> filterConfigs = new LinkedList<>();
        {
            final ResultSelectPlugin.ResultFilterPluginConfig resultFilterConfig1 = new ResultSelectPlugin.ResultFilterPluginConfig();
            resultFilterConfig1.setName("mockResultFilterConfig");
            resultFilterConfig1.setType("com.qingchao.recengine.core.plugin.resultselector.resultfilter.MockResultFilter");
            resultFilterConfig1.setMust(true);
            resultFilterConfig1.setParams(new HashMap<>());
            filterConfigs.add(resultFilterConfig1);

            final ResultSelectPlugin.ResultFilterPluginConfig resultFilterConfig2 = new ResultSelectPlugin.ResultFilterPluginConfig();
            resultFilterConfig2.setName("mockResultFilterConfig");
            resultFilterConfig2.setType("com.qingchao.recengine.core.plugin.resultselector.resultfilter.MockResultFilter");
            resultFilterConfig2.setMust(false);
            resultFilterConfig2.setParams(new HashMap<>());
            filterConfigs.add(resultFilterConfig2);
        }
        config.setFilterConfigs(filterConfigs);

        return config;
    }
}
