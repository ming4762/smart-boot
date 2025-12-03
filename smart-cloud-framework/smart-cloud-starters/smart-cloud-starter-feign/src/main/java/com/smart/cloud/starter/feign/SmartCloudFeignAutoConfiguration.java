package com.smart.cloud.starter.feign;

import com.smart.cloud.starter.feign.codec.BusinessDecoder;
import com.smart.cloud.starter.feign.interceptor.FeignHeaderRequestInterceptor;
import com.smart.cloud.starter.feign.interceptor.FeignTokenRequestInterceptor;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhongming4762
 * 2023/3/10
 */
@Configuration(proxyBeanMethods = false)
@Import({FeignTokenRequestInterceptor.class, FeignHeaderRequestInterceptor.class})
public class SmartCloudFeignAutoConfiguration {

    @Bean
    public Decoder businessDecoder(ObjectProvider<FeignHttpMessageConverters> messageConverters) {
        return new BusinessDecoder(new SpringDecoder(messageConverters));
    }
}
