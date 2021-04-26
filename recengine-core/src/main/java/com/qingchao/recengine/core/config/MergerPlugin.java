package com.qingchao.recengine.core.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Merger插件配置
 */
@Data
public class MergerPlugin implements Serializable, BasePlugin {

    /**
     * 插件名称
     */
    private String name;

    /**
     * 插件实现类
     */
    private String type;

    /**
     * 插件实现类
     */
    private Long size;

    /**
     * 插件信息
     */
    private Map<String, Object> params;
}
