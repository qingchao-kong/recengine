package com.qingchao.recengine.core.bean.candidate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-02 2:37 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class AbstractRecCandidate {

    protected String id;

    private String loaderSource;

    private Double loaderScore;

    private Double rankerScore;

    public AbstractRecCandidate(String id) {
        this.id = id;
    }
}