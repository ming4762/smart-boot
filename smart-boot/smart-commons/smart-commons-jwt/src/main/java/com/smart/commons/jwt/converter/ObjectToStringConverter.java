package com.smart.commons.jwt.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2022/8/9
 * @since 3.0.0
 */
public class ObjectToStringConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, String.class));
    }

    @Override
    public Object convert(Object source, @NonNull TypeDescriptor sourceType, @NonNull TypeDescriptor targetType) {
        return (source != null) ? source.toString() : null;
    }
}
