package com.smart.db.analysis.converter;

/**
 * @author ShiZhongMing
 * 2021/4/30 15:01
 * @since 1.0
 */
public class BooleanToIntegerConverter implements Converter<Boolean, Integer> {
    @Override
    public Integer convert(Boolean resource) {
        if (Boolean.TRUE.equals(resource)) {
            return 1;
        }
        return 0;
    }
}
