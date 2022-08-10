package com.smart.commons.jwt.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * {@link Object}类型转为{@link Instant}
 * @author ShiZhongMing
 * 2022/8/9
 * @since 3.0.0
 */
public class ObjectToInstantConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, Instant.class));
    }

    @Override
    public Object convert(Object source, @NonNull TypeDescriptor sourceType, @NonNull TypeDescriptor targetType) {
        if (source == null || source instanceof Instant) {
            return source;
        }
        if (source instanceof Date date) {
            return date.toInstant();
        }
        if (source instanceof Number number) {
            return Instant.ofEpochSecond(number.longValue());
        }
        try {
            return Instant.ofEpochSecond(Long.parseLong(source.toString()));
        } catch (Exception e) {
            // do nothing
        }
        try {
            return Instant.parse(source.toString());
        } catch (Exception e) {
            // do nothing
        }
        return null;
    }
}
