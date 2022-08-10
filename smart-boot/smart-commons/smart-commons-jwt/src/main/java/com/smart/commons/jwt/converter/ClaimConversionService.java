package com.smart.commons.jwt.converter;

import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * @author ShiZhongMing
 * 2022/8/9 13:34
 * @since 1.0
 */
public class ClaimConversionService extends GenericConversionService {

    private static volatile ClaimConversionService sharedInstance;

    private ClaimConversionService() {
        addConverters(this);
    }


    public static ClaimConversionService getSharedInstance() {
        ClaimConversionService sharedInstance = ClaimConversionService.sharedInstance;
        if (sharedInstance == null) {
            synchronized (ClaimConversionService.class) {
                sharedInstance = ClaimConversionService.sharedInstance;
                if (sharedInstance == null) {
                    sharedInstance = new ClaimConversionService();
                    ClaimConversionService.sharedInstance = sharedInstance;
                }
            }
        }
        return sharedInstance;
    }

    public static void addConverters(ConverterRegistry converterRegistry) {
        converterRegistry.addConverter(new ObjectToInstantConverter());
        converterRegistry.addConverter(new ObjectToStringConverter());
    }
}
