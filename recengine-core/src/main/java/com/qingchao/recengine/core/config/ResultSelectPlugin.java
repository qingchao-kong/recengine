package com.qingchao.recengine.core.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-11 11:41 上午
 */
@Data
public class ResultSelectPlugin implements Serializable, BasePlugin {
    /**
     * 插件名称
     */
    private String name;

    /**
     * 插件实现类
     */
    private String type;

    /**
     * 返回物品最多数量限制
     */
    private Integer size;

    /**
     * 插件信息
     */
    private Map<String, Object> selectParams;

    /**
     * ResultSelect中ResultFilters的配置
     */
    private List<ResultFilterPluginConfig> filterConfigs;

    @Data
    public static class ResultFilterPluginConfig implements Serializable {
        /**
         * 插件名称
         */
        private String name;

        /**
         * 插件实现类
         */
        private String type;

        /**
         * filter是否必须
         */
        private boolean must;

        /**
         * 插件信息
         */
        private Map<String, Object> params;
    }
}
