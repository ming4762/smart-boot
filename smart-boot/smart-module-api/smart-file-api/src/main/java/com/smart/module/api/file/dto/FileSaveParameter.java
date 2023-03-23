package com.smart.module.api.file.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * 文件服务调用文件存储参数
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FileSaveParameter extends FileSaveCommonParameter {

    /**
     * 文件存储器ID，优先级高
     */
    private Long fileStorageId;

    /**
     * 文件存储器编码
     */
    private String fileStorageCode;

    /**
     * 文件类型，默认NORMAL
     */
    @Nullable
    private String type;
}
