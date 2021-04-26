package com.qingchao.recengine.core.bean.request;

import lombok.*;

import java.util.UUID;

/**
 * 基础Request
 * 根据uid+scene唯一确定推荐流数据
 *
 * @author zhangfangfang
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefaultRecRequest {
    @NonNull
    private Long uid;
    private Integer appId;
    private Integer size;

    //todo 补充其他通用属性字段，如性别、年龄等等

    /**
     * 推荐请求唯一id，用于推荐追踪
     */
    private final String requestId = UUID.randomUUID().toString();
}
