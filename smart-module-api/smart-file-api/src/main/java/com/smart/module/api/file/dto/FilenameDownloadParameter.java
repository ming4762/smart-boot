package com.smart.module.api.file.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * 文件名下载参数
 * @author shizhongming
 * 2025/10/15 19:49
 * @since 5.0.0
 */
@Getter
@Setter
public class FilenameDownloadParameter implements Serializable {

    /**
     * 文件存储器代码
     */
    @NonNull
    private String fileStorageCode;
    /**
     * 文件名
     */
    @NonNull
    private String filename;
}
