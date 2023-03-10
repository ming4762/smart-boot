package com.smart.cloud.common.feign;

import com.smart.cloud.common.feign.interceptor.FeignTokenRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhongming4762
 * 2023/3/10
 */
@Configuration(proxyBeanMethods = false)
@Import(FeignTokenRequestInterceptor.class)
public class SmartCloudFeignAutoConfiguration {

}
