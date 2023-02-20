package com.smart.license.server;

import lombok.*;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * License生成参数
 * @author zhongming4762
 * 2022/12/18
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseGeneratorParameter {

    private String subject;

    /**
     * 证书生效日期
     */
    private LocalDateTime issuedTime;

    /**
     * 证书过期日期
     */
    private LocalDateTime expiryTime;

    /**
     * 允许访问的用户数
     */
    private Long consumerAmount;

    /**
     * 描述
     */
    private String description;

    private Long dataId;

    /**
     * 密钥路径
     */
    private InputStream privateKeyInputStream;


    private String storePassword;

    private String keyPassword;

    private String alias;

    /**
     * 证书存储路径
     */
    private String licensePath;
}
