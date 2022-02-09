package com.smart.monitor.server.manager;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shizhongming
 * 2021/3/13 8:10 下午
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
public class MonitorServerManagerAutoConfiguration implements WebMvcConfigurer {

}
