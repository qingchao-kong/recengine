package com.qingchao.recengine.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-19 11:21 上午
 */
@AllArgsConstructor
@Getter
public enum StageEnum {
    requestProcess("requestProcess", "整体请求处理阶段"),
    //    submitContextCallable("submitContextCallable", "提交正常流程失败"),
    contextCallable("contextCallable", ""),
    preContextFiller("preContextFiller", ""),
    loadFromCache("loadFromCache", "从缓存中加载物品"),
    cache("cache", "缓存，用于将推荐流生成的过多的物品缓存，为下次请求使用"),
    oneLoader("oneLoader", ""),
    loaderStage("loaderStage", ""),
    fillerStage("fillerStage", ""),
    oneFiller("oneFiller", ""),
    oneFilter("oneFilter", ""),
    filterStage("filterStage", ""),
    merger("merger", ""),
    ranker("ranker", ""),
    oneReRanker("oneReRanker", ""),
    reRankerStage("reRankerStage", ""),
    resultSelector("resultSelector", ""),
    responseFiller("responseFiller", ""),

    ;

    private String type;
    private String desc;
}
