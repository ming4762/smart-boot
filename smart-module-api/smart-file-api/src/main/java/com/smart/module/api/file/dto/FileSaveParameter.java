package com.smart.module.api.file.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.time.Duration;
import java.time.ZonedDateTime;

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

    @Serial
    private static final long serialVersionUID = 435614322630656879L;
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

    /**
     * 过期时长，优先级比expireAt高
     */
    private Duration expireIn;

    /**
     * 过期时间，优先级比expireIn低
     */
    private ZonedDateTime expireAt;
}
