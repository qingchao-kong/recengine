package com.qingchao.recengine.core.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述:
 * response为list结构
 *
 * @author kongqingchao
 * @create 2021-03-02 2:40 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultRecListResponse<T> {
    private List<T> response;
    private Long uid;
    private Integer appId;
    private Integer size;
}