package com.qingchao.recengine.core.util;

import com.dianping.cat.Cat;
import com.qingchao.recengine.core.config.*;
import com.qingchao.recengine.core.exception.RecEngineConfigException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-26 2:41 下午
 */
@Slf4j
public class ConfigUtil {

    public static boolean checkRecEngineConfig(RecEngineConfig config) {
        try {
            if (Objects.isNull(config)) {
                return false;
            }
            check(config);
        } catch (Throwable throwable) {
            String s = "RecEngine. Config error!";
            log.error(s, throwable);
            Cat.logError(s, throwable);
            return false;
        }
        return true;
    }

    private static void check(RecEngineConfig config) throws Exception {
        //1.PreContextFiller配置校验，可以为空；不为空时，校验type
        if (Objects.nonNull(config.getPreFiller()) && !checkType(config.getPreFiller().getType())) {
            throw new RecEngineConfigException("RecEngine. RecEngine preContextFiller config is null or error.", config);
        }
        //2.loaders配置校验，至少包括一个loader
        if (CollectionUtils.isEmpty(config.getLoader())) {
            throw new RecEngineConfigException("RecEngine. RecEngine loader configs is empty.", config);
        }
        final Set<String> loaderNameSet = new HashSet<>();
        for (LoaderPlugin loaderConfig : config.getLoader()) {
            if (StringUtils.isBlank(loaderConfig.getName()) || !checkType(loaderConfig.getType())) {
                throw new RecEngineConfigException("RecEngine. RecEngine loaders config error, maybe loader name or type is blank.", config);
            }
            if (loaderNameSet.contains(loaderConfig.getName())) {
                throw new RecEngineConfigException("RecEngine. RecEngine loaders config error, loader name duplicate.", config);
            }
            loaderNameSet.add(loaderConfig.getName());
        }
        //3.merger配置校验，可以为空；不为空时，校验type
        if (Objects.nonNull(config.getMerger()) && !checkType(config.getMerger().getType())) {
            throw new RecEngineConfigException("RecEngine. RecEngine merger config type is null or error.", config);
        }
        //4.filler可以不配置，但是配置的话，需要校验type
        if (Objects.nonNull(config.getFiller()) && !checkType(config.getFiller().getType())) {
            throw new RecEngineConfigException("RecEngine. RecEngine filler config error.", config);
        }
        //5.filters可以不配置，但是如果配置的话，需要校验type
        if (CollectionUtils.isNotEmpty(config.getFilter())) {
            for (FilterPlugin filterConfig : config.getFilter()) {
                if (!checkType(filterConfig.getType())) {
                    throw new RecEngineConfigException("RecEngine. RecEngine filter config error.", config);
                }
            }
        }
        //6.ranker可以不配置，但是如果配置的话，需要校验type
        if (Objects.nonNull(config.getRanker()) && !checkType(config.getRanker().getType())) {
            throw new RecEngineConfigException("RecEngine. RecEngine ranker config error.", config);
        }
        //7.reRanker可以不配置，但是如果配置的话，需要校验type
        if (CollectionUtils.isNotEmpty(config.getReRanker())) {
            for (ReRankerPlugin reRanker : config.getReRanker()) {
                if (!checkType(reRanker.getType())) {
                    throw new RecEngineConfigException("RecEngine. RecEngine reRanker config error.", config);
                }
            }
        }
        //8.resultSelector可以不配置；配置时，需要校验type和resultFilter；
        if (Objects.nonNull(config.getResultSelector())
                && (Objects.isNull(config.getResultSelector().getSize()) || config.getResultSelector().getSize() <= 0 || !checkType(config.getResultSelector().getType()))) {
            throw new RecEngineConfigException("RecEngine. RecEngine resultSelector config error.", config);
        }
        if (CollectionUtils.isNotEmpty(config.getResultSelector().getFilterConfigs())) {
            for (ResultSelectPlugin.ResultFilterPluginConfig resultFilterConfig : config.getResultSelector().getFilterConfigs()) {
                if (!checkType(resultFilterConfig.getType())) {
                    throw new RecEngineConfigException("RecEngine. RecEngine resultFilter config error.", config);
                }
            }
        }
        //9.cache可以不配置，但是如果配置的话，需要校验type
        if (Objects.nonNull(config.getCache()) && !checkType(config.getCache().getType())) {
            throw new RecEngineConfigException("RecEngine. RecEngine cache config error.", config);
        }
        //10.responseFiller必须配置
        if (Objects.isNull(config.getResponseFiller()) || !checkType(config.getResponseFiller().getType())) {
            throw new RecEngineConfigException("RecEngine. RecEngine responseFiller config error.", config);
        }
        //11.其他参数校验
        if (Objects.isNull(config.getExtParameter())
                || Objects.isNull(config.getExtParameter().getTimeout())
                || Objects.isNull(config.getExtParameter().getLoaderTimeout())) {
            throw new RecEngineConfigException("RecEngine. RecEngine extParameter config error.", config);
        }
    }

    private static boolean checkType(String type) {
        if (StringUtils.isBlank(type)) {
            return false;
        }
        try {
            return Class.forName(type) != null;
        } catch (Throwable throwable) {
            final String format = String.format("RecEngine. Plugin type config error, type:%s", type);
            log.error(format, throwable);
            Cat.logError(format, throwable);
            return false;
        }
    }
}
