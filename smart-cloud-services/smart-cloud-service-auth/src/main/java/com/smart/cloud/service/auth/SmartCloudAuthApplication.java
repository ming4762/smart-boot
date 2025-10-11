package com.smart.cloud.service.auth;

import com.smart.framework.commons.core.spring.EnableApplicationContext;
import com.smart.framework.commons.core.spring.EnabledCustomObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 认证模块微服务
 * @author zhongming4762
 * 2023/3/4
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnabledCustomObjectMapper
@EnableApplicationContext
public class SmartCloudAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudAuthApplication.class, args);
    }
}
