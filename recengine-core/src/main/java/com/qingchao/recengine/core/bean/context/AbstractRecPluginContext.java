package com.qingchao.recengine.core.bean.context;

import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.config.*;
import com.qingchao.recengine.core.plugin.cache.MockCache;
import com.qingchao.recengine.core.plugin.filler.MockFiller;
import com.qingchao.recengine.core.plugin.filter.DefaultBoolFilter;
import com.qingchao.recengine.core.plugin.loader.MockLoader;
import com.qingchao.recengine.core.plugin.merger.MockMerger;
import com.qingchao.recengine.core.plugin.precontextfiller.MockPreContextFiller;
import com.qingchao.recengine.core.plugin.ranker.MockRanker;
import com.qingchao.recengine.core.plugin.reranker.MockReRanker;
import com.qingchao.recengine.core.plugin.responsefiller.MockResponseFiller;
import com.qingchao.recengine.core.plugin.resultselector.MockResultSelector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.qingchao.recengine.core.constant.ConfigConst.*;


/**
 * 描述:插件上下文
 *
 * @author kongqingchao
 * @create 2021-03-02 2:43 下午
 */
@Component
@Getter
@Slf4j
public class AbstractRecPluginContext implements ApplicationContextAware {
//    @Reference(timeout = 100)
//    private RecConfigDubboService recConfigDubboService;

    private ApplicationContext applicationContext;

    @Value("${dubbo.registry.id}")
    private String protocol;

    @Value("${dubbo.registry.address}")
    private String address;

    @Value("${dubbo.provider.version}")
    private String version;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public final Map<String, RecEngineConfig> getConfig(DefaultRecRequest recRequest, String scene) {
        try {
//            Response<Map<String, RecEngineConfig>> response = recConfigDubboService.queryConfig(recRequest.getUid(), scene);
            //todo 具体实现省略
            Response<Map<String, RecEngineConfig>> response = new Response<>();
            if (Objects.nonNull(response)
                    && response.isSuccess()
                    && MapUtils.isNotEmpty(response.getResult())
                    && response.getResult().containsKey(RESULT)
                    && response.getResult().containsKey(DEFAULT)
                    && response.getResult().containsKey(COMPENSATE)) {
                return response.getResult();
            }
        } catch (Throwable throwable) {
            final String format = String.format("RecEngine. Get config error, scene:%s, uid:%s", scene, recRequest.getUid());
            log.error(format, throwable);
            Cat.logError(format, throwable);
        }
        return null;
    }

    public final Map<String, RecEngineConfig> mockConfig(DefaultRecRequest recRequest, String scene) {
        return new HashMap<String, RecEngineConfig>() {{
            put("RESULT", mockRecEngineConfig(scene));
            put("DEFAULT", mockRecEngineConfig(scene));
            put("COMPENSATE", mockRecEngineConfig(scene));
        }};
    }

    private RecEngineConfig mockRecEngineConfig(String scene) {
        final RecEngineConfig recEngineConfig = new RecEngineConfig();
        recEngineConfig.setUid(1111L);
        recEngineConfig.setScene(scene);
        recEngineConfig.setPreFiller(MockPreContextFiller.mockConfig());
        List<LoaderPlugin> loaderConfigs = new LinkedList<>();
        {
            loaderConfigs.add(MockLoader.mockConfig("MockLoader1"));
            loaderConfigs.add(MockLoader.mockConfig("MockLoader2"));
            loaderConfigs.add(MockLoader.mockConfig("MockLoader3"));
        }
        recEngineConfig.setLoader(loaderConfigs);
        recEngineConfig.setMerger(MockMerger.mockConfig());
        recEngineConfig.setFiller(MockFiller.mockConfig());
        List<FilterPlugin> filterConfigs = new LinkedList<>();
        {
            filterConfigs.add(DefaultBoolFilter.mockConfig());
        }
        recEngineConfig.setFilter(filterConfigs);
        recEngineConfig.setRanker(MockRanker.mockConfig());
        List<ReRankerPlugin> rerankConfigs = new LinkedList<>();
        {
            rerankConfigs.add(MockReRanker.mockConfig());
        }
        recEngineConfig.setReRanker(rerankConfigs);
        recEngineConfig.setResultSelector(MockResultSelector.mockConfig());
        recEngineConfig.setResponseFiller(MockResponseFiller.mockConfig());
        recEngineConfig.setCache(MockCache.mockConfig());

        final ExtParameter extParameter = new ExtParameter();
        {
            extParameter.setTimeout(1000000L);
            extParameter.setLoaderTimeout(1000000L);
            extParameter.setCache(false);
        }
        recEngineConfig.setExtParameter(extParameter);

        return recEngineConfig;
    }
}
