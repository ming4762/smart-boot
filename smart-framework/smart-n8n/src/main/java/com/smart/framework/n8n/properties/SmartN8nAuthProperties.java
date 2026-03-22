package com.smart.framework.n8n.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

/**
 * N8n Auth Properties
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-07 20:19
 * @since 5.0.0
 */
@Getter
@Setter
public class SmartN8nAuthProperties implements Serializable {

    /**
     * AUTH BASIC
     */
    @NestedConfigurationProperty
    private SmartN8nAuthBasicProperties basic;

    /**
     * AUTH HEADER
     */
    @NestedConfigurationProperty
    private SmartN8nAuthHeaderProperties header;
}
