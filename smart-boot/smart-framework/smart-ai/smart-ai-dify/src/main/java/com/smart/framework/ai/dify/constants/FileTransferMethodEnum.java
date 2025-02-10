package com.smart.framework.ai.dify.constants;

import lombok.Getter;

/**
 * 文件传输方式
 * @author shizhongming
 * 2025/2/8 19:37
 * @since 5.0.0
 */
@Getter
public enum FileTransferMethodEnum implements EnumValue {

    /**
     * 图片地址
     */
    REMOTE_URL("remote_url"),
    /**
     * 上传文件。
     */
    LOCAL_FILE("local_file"),
    ;

    private final String value;
    FileTransferMethodEnum(String value) {
        this.value = value;
    }
}
