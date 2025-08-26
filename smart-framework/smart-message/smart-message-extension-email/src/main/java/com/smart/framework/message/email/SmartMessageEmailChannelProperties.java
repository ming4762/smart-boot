package com.smart.framework.message.email;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 邮件消息通道信息
 * @author shizhongming
 * 2024/10/31 9:16
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode
public class SmartMessageEmailChannelProperties {

    /**
     * SMTP server host. For instance, 'smtp.example.com'.
     */
    private String host;

    /**
     * SMTP server port.
     */
    private Integer port;

    /**
     * Login user of the SMTP server.
     */
    private String username;

    /**
     * Login password of the SMTP server.
     */
    private String password;

    /**
     * Protocol used by the SMTP server.
     */
    private String protocol = "smtp";

    /**
     * Default MimeMessage encoding.
     */
    private Charset defaultEncoding = StandardCharsets.UTF_8;

    /**
     * Additional JavaMail Session properties.
     */
    private String properties;
}
