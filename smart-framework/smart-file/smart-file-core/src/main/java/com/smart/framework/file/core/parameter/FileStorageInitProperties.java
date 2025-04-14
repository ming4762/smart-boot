package com.smart.framework.file.core.parameter;

import lombok.*;

import java.io.Serializable;

/**
 * 文件存储初始化参数
 * @author shizhongming
 * 2025/4/13 20:07
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FileStorageInitProperties implements Serializable {

    private String properties;

    private boolean encryptedYn;

    private String privateKey;

    private String publicKey;

    private Long fileStorageId;
}
