package com.smart.framework.file.core.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 文件查询参数
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
@SuperBuilder
public class FileStorageGetParameter extends FileStorageCommonParameter {

    @Serial
    private static final long serialVersionUID = 5327844538893435761L;

    private String storageStoreKey;

    /**
     * 是否加密
     */
    private boolean encryptedYn;
}
