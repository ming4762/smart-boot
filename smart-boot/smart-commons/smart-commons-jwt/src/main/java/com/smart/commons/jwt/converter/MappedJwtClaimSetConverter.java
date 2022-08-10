package com.smart.commons.jwt.converter;

import com.smart.commons.jwt.claim.ClaimNames;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ShiZhongMing
 * 2022/8/9 13:37
 * @since 1.0
 */
public class MappedJwtClaimSetConverter implements Converter<Map<String, Object>, Map<String, Object>> {

    private static final ConversionService CONVERSION_SERVICE = ClaimConversionService.getSharedInstance();

    private static final TypeDescriptor OBJECT_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(Object.class);

    private static final TypeDescriptor INSTANT_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(Instant.class);

    private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);

    private final Map<String, Converter<Object, ?>> claimTypeConverters;

    public MappedJwtClaimSetConverter(Map<String, Converter<Object, ?>> claimTypeConverters) {
        Assert.notNull(claimTypeConverters, "claimTypeConverters cannot be null");
        this.claimTypeConverters = claimTypeConverters;
    }

    public static MappedJwtClaimSetConverter withDefaults(Map<String, Converter<Object, ?>> claimTypeConverters) {
        Assert.notNull(claimTypeConverters, "claimTypeConverters cannot be null");

        Converter<Object, ?> stringConverter = getConverter(STRING_TYPE_DESCRIPTOR);
        Map<String, Converter<Object, ?>> claimNameToConverter = new HashMap<>();
        claimNameToConverter.put(ClaimNames.EXP, MappedJwtClaimSetConverter::convertInstant);
        claimNameToConverter.put(ClaimNames.IAT, MappedJwtClaimSetConverter::convertInstant);

        claimNameToConverter.put(ClaimNames.SUB, stringConverter);
        claimNameToConverter.put(ClaimNames.JTI, stringConverter);

        claimNameToConverter.putAll(claimTypeConverters);
        return new MappedJwtClaimSetConverter(claimNameToConverter);
    }

    private static Converter<Object, ?> getConverter(TypeDescriptor targetDescriptor) {
        return (source) -> CONVERSION_SERVICE.convert(source, OBJECT_TYPE_DESCRIPTOR, targetDescriptor);
    }

    private static Instant convertInstant(Object source) {
        if (source == null) {
            return null;
        }
        Instant result = (Instant) CONVERSION_SERVICE.convert(source, OBJECT_TYPE_DESCRIPTOR, INSTANT_TYPE_DESCRIPTOR);
        Assert.state(result != null, () -> "Could not coerce " + source + " into an Instant");
        return result;
    }

    @Override
    public Map<String, Object> convert(@NonNull Map<String, Object> claims) {
        Assert.notNull(claims, "claims cannot be null");
        Map<String, Object> mappedClaims = new HashMap<>(claims);

        for (Map.Entry<String, Converter<Object, ?>> entry : this.claimTypeConverters.entrySet()) {
            String claimName = entry.getKey();
            Converter<Object, ?> converter = entry.getValue();
            if (converter != null) {
                Object claim = claims.get(claimName);
                Object mappedClaim = converter.convert(claim);
                mappedClaims.compute(claimName, (key, value) -> mappedClaim);
            }
        }
        return mappedClaims;
    }
}
