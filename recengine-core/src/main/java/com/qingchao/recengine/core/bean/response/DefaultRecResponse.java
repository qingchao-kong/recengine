package com.qingchao.recengine.core.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-02 2:40 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultRecResponse<T> {
    private T response;
    private Long uid;
    private Integer appId;
    private Integer size;
}