package com.qingchao.recengine.core.plugin;

import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.config.FilterPlugin;
import com.qingchao.recengine.core.config.LoaderPlugin;
import com.qingchao.recengine.core.config.ReRankerPlugin;
import com.qingchao.recengine.core.config.RecEngineConfig;
import com.qingchao.recengine.core.exception.RecEngineConfigException;
import com.qingchao.recengine.core.plugin.cache.AbstractCache;
import com.qingchao.recengine.core.plugin.filler.AbstractFiller;
import com.qingchao.recengine.core.plugin.filter.AbstractFilter;
import com.qingchao.recengine.core.plugin.loader.AbstractLoader;
import com.qingchao.recengine.core.plugin.merger.AbstractMerger;
import com.qingchao.recengine.core.plugin.precontextfiller.AbstractPreContextFiller;
import com.qingchao.recengine.core.plugin.ranker.AbstractRanker;
import com.qingchao.recengine.core.plugin.reranker.AbstractReRanker;
import com.qingchao.recengine.core.plugin.responsefiller.AbstractResponseFiller;
import com.qingchao.recengine.core.plugin.resultselector.AbstractResultSelector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-02 4:02 下午
 */
@Getter
@Slf4j
public class RecPluginManager<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext> {
    protected AbstractPreContextFiller preContextFiller;
    protected List<AbstractLoader> loaders;
    protected AbstractMerger merger = null;
    protected List<AbstractFiller> fillers = new ArrayList<>();
    protected List<AbstractFilter> filters = new ArrayList<>();
    protected AbstractRanker ranker = null;
    protected List<AbstractReRanker> reRankers = new ArrayList<>();
    protected AbstractResultSelector resultSelector = null;
    protected AbstractResponseFiller responseFiller;
    protected AbstractCache cache = null;

    public RecPluginManager(RecEngineConfig recEngineConfig, PC pluginContext, Class<RC> requestContextClass) throws Exception {
        //实例化preContextFiller，必须有
        final AbstractPreContextFiller preContextFiller = (AbstractPreContextFiller) Class.forName(recEngineConfig.getPreFiller().getType()).newInstance();
        preContextFiller.init(recEngineConfig.getPreFiller(), pluginContext, requestContextClass);
        this.preContextFiller = preContextFiller;

        //实例化loaders，必须有
        if (recEngineConfig.getLoader().size() == 0) {
            throw new RecEngineConfigException("RecEngine. Loader list size is zero!", recEngineConfig);
        }
        List<AbstractLoader> tmpLoaders = new LinkedList<>();
        final List<LoaderPlugin> loaderConfigList = recEngineConfig.getLoader();
        loaderConfigList.forEach(loaderConfig -> {
            try {
                final AbstractLoader loader = (AbstractLoader) Class.forName(loaderConfig.getType()).newInstance();
                loader.init(loaderConfig, pluginContext);
                tmpLoaders.add(loader);
            } catch (Throwable throwable) {
                final String format = String.format("PluginManager. Init loader error. error:%s, loaderConfig:%s", throwable, loaderConfig);
                log.error(format, throwable);
                Cat.logError(format, throwable);
            }
        });
        if (loaderConfigList.size() > tmpLoaders.size()) {
            throw new RecEngineConfigException("PluginManager. Init loaders error. Please check loader configs! Return recRequest false.", recEngineConfig);
        }
        this.loaders = tmpLoaders;

        //实例化merger，必须有
        final AbstractMerger merger = (AbstractMerger) Class.forName(recEngineConfig.getMerger().getType()).newInstance();
        merger.init(recEngineConfig.getMerger(), pluginContext);
        this.merger = merger;

        //实例化filler，可以不配置
        //todo
//        if (Objects.nonNull(recEngineConfig.getFiller())) {
//            final AbstractFiller filler = (AbstractFiller) Class.forName(recEngineConfig.getFiller().getType()).newInstance();
//            filler.init(recEngineConfig.getFiller(), pluginContext);
//            this.filler = filler;
//        }

        //实例化filters，0-n个
        if (CollectionUtils.isNotEmpty(recEngineConfig.getFilter())) {
            for (FilterPlugin config : recEngineConfig.getFilter()) {
                final AbstractFilter filter = (AbstractFilter) Class.forName(config.getType()).newInstance();
                filter.init(config, pluginContext);
                this.filters.add(filter);
            }
        }

        //实例化ranker，可以不配置
        if (Objects.nonNull(recEngineConfig.getRanker())) {
            final AbstractRanker ranker = (AbstractRanker) Class.forName(recEngineConfig.getRanker().getType()).newInstance();
            ranker.init(recEngineConfig.getRanker(), pluginContext);
            this.ranker = ranker;
        }

        //实例化reRankers，0-n个
        if (CollectionUtils.isNotEmpty(recEngineConfig.getReRanker())) {
            for (ReRankerPlugin config : recEngineConfig.getReRanker()) {
                final AbstractReRanker reRanker = (AbstractReRanker) Class.forName(config.getType()).newInstance();
                reRanker.init(config, pluginContext);
                this.reRankers.add(reRanker);
            }
        }

        //实例化resultSelect，必须配置
        final AbstractResultSelector resultSelector = (AbstractResultSelector) Class.forName(recEngineConfig.getResultSelector().getType()).newInstance();
        resultSelector.init(recEngineConfig.getResultSelector(), pluginContext);
        this.resultSelector = resultSelector;

        //实例化cache，cache可以不配置
        if (Objects.nonNull(recEngineConfig.getCache())) {
            final AbstractCache cache = (AbstractCache) Class.forName(recEngineConfig.getCache().getType()).newInstance();
            cache.init(recEngineConfig.getCache(), pluginContext);
            this.cache = cache;
        }

        //实例化responseFiller，必须有
        final AbstractResponseFiller responseFiller = (AbstractResponseFiller) Class.forName(recEngineConfig.getResponseFiller().getType()).newInstance();
        responseFiller.init(recEngineConfig.getResponseFiller(), pluginContext);
        this.responseFiller = responseFiller;
    }
}
