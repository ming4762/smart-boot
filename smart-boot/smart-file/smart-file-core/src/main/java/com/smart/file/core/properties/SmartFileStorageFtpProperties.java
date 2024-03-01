package com.smart.file.core.properties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * FTP存储器配置
 * @author shizhongming
 * 2023/12/28 17:30
 * @since 3.0.0
 */
@Getter
@Setter
public class SmartFileStorageFtpProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = -1453106334682555483L;
    private String host;

    private Integer port = 21;

    private String basePath;

    private String username;

    private String password;

    private String encoding;

}
