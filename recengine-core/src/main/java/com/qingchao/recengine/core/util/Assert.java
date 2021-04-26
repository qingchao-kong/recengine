package com.qingchao.recengine.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-04-26 11:30 上午
 */
public class Assert {
    /**
     * 断言字符串不为空白
     *
     * @param str     要检查的字符串
     * @param message 字符串为空白时抛出的异常信息
     * @throws IllegalArgumentException 如果<code>str</code>为空，抛异常
     * @see StringUtils#isBlank(CharSequence)
     */
    public static void notBlank(CharSequence str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(message);
        }
    }
}
