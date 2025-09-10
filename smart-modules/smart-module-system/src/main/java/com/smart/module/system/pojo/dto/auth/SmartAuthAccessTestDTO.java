package com.smart.module.system.pojo.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author shizhongming
 * 2025/9/9 19:02
 * @since 5.0.0
 */
@Getter
@Setter
public class SmartAuthAccessTestDTO implements Serializable {

    private String queryParameter;

    private String jsonParameter;

    private Long accessId;

    private String tokenPrefix;

}
