package com.smart.framework.commons.jwt.converter;

import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * @author ShiZhongMing
 * 2022/8/9 13:34
 * @since 1.0
 */
public class ClaimConversionService extends GenericConversionService {

    private ClaimConversionService() {
        addConverters(this);
    }

    private static class Holder {
        private static final ClaimConversionService INSTANCE = new ClaimConversionService();
    }

    public static ClaimConversionService getSharedInstance() {
        return Holder.INSTANCE;
    }

    public static void addConverters(ConverterRegistry converterRegistry) {
        converterRegistry.addConverter(new ObjectToInstantConverter());
        converterRegistry.addConverter(new ObjectToStringConverter());
    }
}
