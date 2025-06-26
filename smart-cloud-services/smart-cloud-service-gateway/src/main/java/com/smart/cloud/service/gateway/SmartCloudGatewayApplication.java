package com.smart.cloud.service.gateway;

import com.smart.cloud.starter.feign.config.EnabledSyncFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 路由中心
 * @author zhongming4762
 * 2023/3/4
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnabledSyncFeign
public class SmartCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudGatewayApplication.class, args);
    }
}
