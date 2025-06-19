package com.smart.module.file.pojo;

import com.smart.framework.file.core.service.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author shizhongming
 * 2025/6/19 21:18
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
public class FileStorageServiceCacheData implements Serializable {


    private Long id;

    private String code;

    private FileStorageService fileStorageService;
}
