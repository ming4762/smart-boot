package com.smart.framework.commons.core.spring;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.smart.framework.commons.core.constants.DateTimePatternEnum;
import com.smart.framework.commons.core.json.BaseEnumJsonModule;
import com.smart.framework.commons.core.utils.JsonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

/**
 * @author zhongming4762
 * 2022/12/17 20:02
 */
@Configuration
public class CustomObjectMapperConfigurer implements WebMvcConfigurer {

    private static final DateTimeFormatter ISO_8601_FORMATTER = new DateTimeFormatterBuilder().appendInstant(3).toFormatter();
    /**
     *
     * @param converters initially an empty list of converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper());
        converters.add(jackson2HttpMessageConverter);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // BigDecimal处理
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        // 浮点数默认反序列化为BigDecimal
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 设置日期序列化和反序列化
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateTimePatternEnum.DATE_TIME.getPattern()).withZone(ZoneId.systemDefault())));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateTimePatternEnum.DATE.getPattern()).withZone(ZoneId.systemDefault())));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateTimePatternEnum.TIME.getPattern()).withZone(ZoneId.systemDefault())));
        javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(ISO_8601_FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_DATE.withZone(ZoneId.systemDefault())));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_TIME.withZone(ZoneId.systemDefault())));
        objectMapper.registerModule(javaTimeModule);

        // 注册BaseEnum模块
        SimpleModule baseEnumModule = new BaseEnumJsonModule();
        objectMapper.registerModule(baseEnumModule);

        // 初始化JsonUtils
        JsonUtils.initObjectMapper(objectMapper);

        return objectMapper;
    }
}
