package com.smart.module.api.file.bo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 文件保存结果
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FileHandlerResult implements Serializable {

    private Long fileId;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 类型
     */
    private String type;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 保存到文件存储器的KEY
     */
    private String storageStoreKey;

    /**
     * 文件存储器ID
     */
    private Long fileStorageId;
}
