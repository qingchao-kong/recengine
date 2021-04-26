package com.qingchao.recengine.core.plugin.ranker;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.RankerPlugin;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-04 11:04 上午
 */
public class MockRanker<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractRanker<RC,PC,R,C> {
    @Override
    public void init(RankerPlugin rankerConfig, PC pluginContext) {

    }

    @Override
    public boolean rank(RC requestContext, List<C> input, List<C> output) {
        output.addAll(input);
        return true;
    }

    public static RankerPlugin mockConfig() {
//        final RankerPlugin config = new RankerPlugin();
//        config.setName("MockRanker");
//        config.setType("com.qingchao.recengine.core.plugin.ranker.MockRanker");
//        config.setParams(new HashMap<>());

        RankerPlugin config = new RankerPlugin();
        config.setName("followGodPredictNewSdkRanker");
        config.setType("com.qingchao.recengine.core.plugin.ranker.DefaultPredictRanker");
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("modelName","dub_ps_smart");
        stringStringHashMap.put("modelType","ps_smart");
        stringStringHashMap.put("interfaceReference","com.qingchao.rec.predict.api.ChatRankService");
        config.setParams(stringStringHashMap);
        return config;
    }
}
