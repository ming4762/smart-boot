package com.smart.file.core.pojo.bo;

import com.smart.file.core.constants.FileTypeEnum;
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
    private String fileName;

    /**
     * 类型
     */
    private FileTypeEnum type;

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