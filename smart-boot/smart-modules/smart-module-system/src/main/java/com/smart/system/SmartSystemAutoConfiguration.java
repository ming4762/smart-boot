package com.smart.system;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 系统模块自动配置类
 * @author jackson
 * 2020/1/22 9:44 上午
 */
@Configuration
@AutoConfigureBefore(JacksonAutoConfiguration.class)
@ComponentScan
public class SmartSystemAutoConfiguration {

}
