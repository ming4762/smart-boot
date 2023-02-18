package com.smart.file.core.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 文件查询参数
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
@SuperBuilder
public class FileStorageGetParameter extends FileStorageCommonParameter {


    private String fileStorageKey;
}
