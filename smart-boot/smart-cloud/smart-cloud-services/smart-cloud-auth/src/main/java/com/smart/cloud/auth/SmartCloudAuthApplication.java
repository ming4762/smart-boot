package com.smart.cloud.auth;

import com.smart.commons.core.spring.EnabledCustomObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证模块微服务
 * @author zhongming4762
 * 2023/3/4
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnabledCustomObjectMapper
@EnableFeignClients(basePackages = {"com.smart.cloud.api.system.feign"})
public class SmartCloudAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudAuthApplication.class, args);
    }
}
