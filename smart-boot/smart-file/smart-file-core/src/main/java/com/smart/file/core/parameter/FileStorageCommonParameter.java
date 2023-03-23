package com.smart.file.core.parameter;

import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/2/16 22:12
 */
@Getter
@Setter
@SuperBuilder
public class FileStorageCommonParameter implements Serializable {

    /**
     * 文件存储器类型
     */
    @NonNull
    private FileStorageTypeEnum storageType;

    /**
     * 文件存储器参数
     */
    @NonNull
    private String storageProperties;



}
