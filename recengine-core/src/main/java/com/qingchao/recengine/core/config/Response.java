package com.qingchao.recengine.core.config;

import lombok.Data;

import java.util.Map;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-04-26 10:46 上午
 */
@Data
public class Response<T> {
    private String code;
    private String msg;
    private boolean success;
    private T result;
    private String tid;
    private Map<String, Object> ext;
}
