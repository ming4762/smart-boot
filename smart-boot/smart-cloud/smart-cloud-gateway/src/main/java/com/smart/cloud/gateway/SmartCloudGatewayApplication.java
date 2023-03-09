package com.smart.cloud.gateway;

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
public class SmartCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudGatewayApplication.class, args);
    }
}
