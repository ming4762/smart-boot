package com.smart.db.analysis.converter;

/**
 * @author ShiZhongMing
 * 2021/4/30 15:05
 * @since 1.0
 */
public class IntegerToLongConverter implements Converter<Integer, Long> {
    @Override
    public Long convert(Integer resource) {
        if (resource == null) {
            return null;
        }
        return resource.longValue();
    }
}
