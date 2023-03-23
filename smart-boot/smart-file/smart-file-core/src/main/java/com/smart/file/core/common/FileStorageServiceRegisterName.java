package com.smart.file.core.common;

import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件存储器注册名字
 * @author shizhongming
 * 2020/11/29 3:40 上午
 */
@Getter
@Setter
@Builder
public class FileStorageServiceRegisterName {

    /**
     * spring bean 名称
     */
    private String beanName;

    /**
     * 文件存储器类型
     */
    private FileStorageTypeEnum storageType;
}
