package com.qingchao.recengine.core.exception;

import com.alibaba.fastjson.JSON;
import com.qingchao.recengine.core.config.RecEngineConfig;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-26 2:55 下午
 */
public class RecEngineConfigException extends Exception {
    public RecEngineConfigException(String message, RecEngineConfig config) {
        super(message + " Config:" + JSON.toJSONString(config));
    }
}
