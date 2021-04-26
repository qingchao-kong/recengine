package com.qingchao.recengine.core.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RecEngineConfig implements Serializable {
    /**
     * 请求用户uid
     */
    private Long uid;

    /**
     * 当前推荐场景
     */
    private String scene;

    /**
     * context填充
     */
    private PreFillerPlugin preFiller;

    /**
     * 召回阶段的配置
     */
    private List<LoaderPlugin> loader;

    /**
     * 合并的配置
     */
    private MergerPlugin merger;

    /**
     * 填充配置
     */
    private FillerPlugin filler;

    /**
     * 过滤配置
     */
    private List<FilterPlugin> filter;

    /**
     * 排序配置
     */
    private RankerPlugin ranker;

    /**
     * 重排序配置
     */
    private List<ReRankerPlugin> reRanker;

    /**
     * 规则调整
     */
    private ResultSelectPlugin resultSelector;

    /**
     * response填充
     */
    private ResponseFillerPlugin responseFiller;

    /**
     * 结果缓存和已读配置
     */
    private CachePlugin cache;

    /**
     * 其他配置
     */
    private ExtParameter extParameter;


}
