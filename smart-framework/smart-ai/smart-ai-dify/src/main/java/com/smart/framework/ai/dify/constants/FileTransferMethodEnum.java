package com.smart.framework.ai.dify.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smart.framework.ai.dify.json.EnumValueJson;
import lombok.Getter;

/**
 * 文件传输方式
 * @author shizhongming
 * 2025/2/8 19:37
 * @since 5.0.0
 */
@Getter
@JsonDeserialize(using = EnumValueJson.FileTransferMethodEnumEnumValueDeserializer.class)
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

    public static FileTransferMethodEnum getByValue(String value) {
        for (FileTransferMethodEnum fileTransferMethodEnum : FileTransferMethodEnum.values()) {
            if (fileTransferMethodEnum.getValue().equals(value)) {
                return fileTransferMethodEnum;
            }
        }
        return null;
    }
}
