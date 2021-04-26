package com.qingchao.recengine.core.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author kongqingchao
 * @date 2021/3/24
 */
@Data
public class RankerPlugin implements Serializable , BasePlugin {
    /**
     * 插件名称
     */
    private String name;

    /**
     * 插件实现类
     */
    private String type;

    /**
     * 插件信息
     */
    private Map<String, Object> params;
}
