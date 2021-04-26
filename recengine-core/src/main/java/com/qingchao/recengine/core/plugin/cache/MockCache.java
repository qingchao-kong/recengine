package com.qingchao.recengine.core.plugin.cache;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.CachePlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 描述:do nothing
 *
 * @author kongqingchao
 * @create 2021-03-04 11:00 上午
 */
public class MockCache<RC extends AbstractRecRequestContext, PC extends AbstractRecPluginContext,
        R extends DefaultRecRequest, C extends AbstractRecCandidate> extends AbstractCache<RC, PC, C> {
    @Override
    public void init(CachePlugin cacherConfig, PC pluginContext) {

    }

    @Override
    public boolean cacheCandidates(RC requestContext, List<C> input, List<C> output) {
        output.addAll(input);
        return true;
    }

    @Override
    public int getMinLoadSize() {
        return 10;
    }

    @Override
    public boolean retrieveCandidates(RC requestContext, List<C> output) {
        Random random = new Random();
        final boolean useCache = random.nextBoolean();
        if (useCache) {
            output.add((C) new AbstractRecCandidate("1"));
            output.add((C) new AbstractRecCandidate("2"));
            output.add((C) new AbstractRecCandidate("3"));
            output.add((C) new AbstractRecCandidate("4"));
            output.add((C) new AbstractRecCandidate("5"));
            output.add((C) new AbstractRecCandidate("6"));
            output.add((C) new AbstractRecCandidate("7"));
            output.add((C) new AbstractRecCandidate("8"));
            output.add((C) new AbstractRecCandidate("9"));
            output.add((C) new AbstractRecCandidate("10"));
            output.add((C) new AbstractRecCandidate("11"));
            output.add((C) new AbstractRecCandidate("12"));
            output.add((C) new AbstractRecCandidate("13"));
            output.add((C) new AbstractRecCandidate("14"));
            output.add((C) new AbstractRecCandidate("15"));
            output.add((C) new AbstractRecCandidate("16"));
            output.add((C) new AbstractRecCandidate("17"));
            output.add((C) new AbstractRecCandidate("18"));
            output.add((C) new AbstractRecCandidate("19"));
            output.add((C) new AbstractRecCandidate("20"));
            output.add((C) new AbstractRecCandidate("21"));
            output.add((C) new AbstractRecCandidate("22"));
            output.add((C) new AbstractRecCandidate("23"));
            output.add((C) new AbstractRecCandidate("24"));
            output.add((C) new AbstractRecCandidate("25"));
            output.add((C) new AbstractRecCandidate("26"));
            output.add((C) new AbstractRecCandidate("27"));
            output.add((C) new AbstractRecCandidate("28"));
            output.add((C) new AbstractRecCandidate("29"));
            output.add((C) new AbstractRecCandidate("30"));
            return true;
        } else {
            return false;
        }
    }

    public static CachePlugin mockConfig() {
        final CachePlugin config = new CachePlugin();
        config.setName("MockCache");
        config.setType("com.qingchao.recengine.core.plugin.cache.MockCache");
        config.setParams(new HashMap<>());
        return config;
    }
}
