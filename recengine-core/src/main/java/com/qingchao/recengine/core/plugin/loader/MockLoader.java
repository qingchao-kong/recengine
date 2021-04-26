package com.qingchao.recengine.core.plugin.loader;

import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.LoaderPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-15 2:54 下午
 */
public class MockLoader<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext,
        C extends AbstractRecCandidate> extends AbstractLoader<PC, RC, C> {
    private static final Random random = new Random();

    private String name;

    @Override
    public void init(LoaderPlugin loaderConfig, AbstractRecPluginContext pluginContext) {
        this.name = loaderConfig.getName();
    }

    @Override
    public boolean load(RC requestContext, List<C> output) {
        {
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
            output.add((C) new AbstractRecCandidate(String.valueOf(random.nextInt(100))));
        }
        return true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static LoaderPlugin mockConfig(String name) {
        final LoaderPlugin tsLoaderConfig = new LoaderPlugin();
        tsLoaderConfig.setName(name);
        tsLoaderConfig.setType("com.qingchao.recengine.core.plugin.loader.MockLoader");
        tsLoaderConfig.setParams(new HashMap<>());
        return tsLoaderConfig;
    }
}
