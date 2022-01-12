package com.smart.db.analysis.converter;

/**
 * @author ShiZhongMing
 * 2021/6/10 9:42
 * @since 1.0
 */
public class LongToIntegerConverter implements Converter<Long, Integer> {
    @Override
    public Integer convert(Long resource) {
        if (resource == null) {
            return null;
        }
        return resource.intValue();
    }
}
