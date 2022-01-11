package com.smart.db.analysis.constants;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * extJS 与 java映射
 * @author shizhongming
 * 2020/7/30 8:39 下午
 */
@Getter
public enum ExtMappingEnum {

    /**
     * extJS 与 java映射
     */
    BOOLEAN("boolean", Boolean.class),
    INTEGER("int", Integer.class),
    LONG("int", Long.class),
    DOUBLE("number", Double.class),
    FLOAT("number", Float.class),
    BIG_DECIMAL("number", java.math.BigDecimal.class),
    LOCAL_DATE("date", LocalDate.class),
    LOCAL_TIME("date", LocalTime.class),
    LOCAL_DATE_TIME("date", LocalDateTime.class),
    STRING("string", String.class);

    private final String extClass;

    private final Class<?> javaClass;

    ExtMappingEnum(String extClass, Class<?> javaClass) {
        this.extClass = extClass;
        this.javaClass = javaClass;
    }
}
