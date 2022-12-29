package com.smart.license.core.model;

import lombok.*;

import java.io.Serializable;

/**
 * 校验license参数-项目信息
 * @author zhongming4762
 * 2022/12/28 20:18
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LicenseCheckProjectInfo implements Serializable {

    /**
     * 企业
     */
    private String enterprise;

    /**
     * 项目&系统
     */
    private String project;

    /**
     * 系统版本号
     */
    private String version;
}
