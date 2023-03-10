package com.smart.cloud.gateway;

import com.smart.cloud.common.feign.config.EnabledSyncFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 路由中心
 * @author zhongming4762
 * 2023/3/4
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnabledSyncFeign
@EnableFeignClients(basePackages = "com.smart.cloud.api.auth.feign")
public class SmartCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudGatewayApplication.class, args);
    }
}
