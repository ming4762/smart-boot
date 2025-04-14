package com.smart.framework.file.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * 文件存储保存结果
 * @author shizhongming
 * 2025/4/13 20:57
 * @since 5.0.0
 */
@Getter
@AllArgsConstructor
@Builder
public class FileStorageSaveResult implements Serializable {

    /**
     * 文件存储key
     */
    private String fileStoreKey;

    private Long fileStorageId;

    private boolean encryptedYn;
}
