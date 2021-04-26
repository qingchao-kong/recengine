package com.qingchao.recengine.core.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author kongqingchao
 * @date 2021/3/24
 */
@Data
public class ExtParameter implements Serializable {
    /**
     * 整个推荐流超时时间
     */
    private Long timeout;

    /**
     * 召回超时时间
     */
    private Long loaderTimeout;

    /**
     * 结果为空时兜底开关
     */
    private Boolean compensate;

    /**
     * 缓存开关
     */
    private Boolean cache;

    /**
     * 其他新增配置
     */
    private Map<String, Object> others;
}
