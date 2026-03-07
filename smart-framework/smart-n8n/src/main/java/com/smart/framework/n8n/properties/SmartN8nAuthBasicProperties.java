package com.smart.framework.n8n.properties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * N8n Auth Basic Properties
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-07 20:19
 * @since 5.0.0
 */
@Getter
@Setter
public class SmartN8nAuthBasicProperties implements Serializable {

    private String user;

    private String password;

}
