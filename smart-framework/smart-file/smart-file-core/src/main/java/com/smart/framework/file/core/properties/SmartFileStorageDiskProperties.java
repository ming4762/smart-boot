package com.smart.framework.file.core.properties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/2/16 22:09
 */
@Getter
@Setter
public class SmartFileStorageDiskProperties implements Serializable {

    /**
     * 本地磁盘存储路径
     */
    private String basePath;
}
