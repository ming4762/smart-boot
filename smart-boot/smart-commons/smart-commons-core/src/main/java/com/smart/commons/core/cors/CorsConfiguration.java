package com.smart.commons.core.cors;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author jackson
 * 2020/2/16 3:27 下午
 */
@Configuration
public class CorsConfiguration {

    @Bean("corsFilterRegistrationBean")
    public FilterRegistrationBean<Filter> corsFilterRegistrationBean() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("corsFilterRegistrationBean");
        registration.setOrder(Integer.MIN_VALUE);
        return registration;
    }
}
