package com.smart.commons.core.constants;

import lombok.Getter;

/**
 * 时间类型转换格式
 * @author zhongming4762
 * 2022/12/17 19:43
 */
public enum DateTimePatternEnum {
    /**
     * 日期时间格式
     */
    DATE_TIME("yyyy-MM-dd HH:mm:ss"),
    DATE("yyyy-MM-dd"),
    TIME("HH:mm:ss")
    ;

    @Getter
    private final String pattern;

    DateTimePatternEnum(String pattern) {
        this.pattern = pattern;
    }
}
