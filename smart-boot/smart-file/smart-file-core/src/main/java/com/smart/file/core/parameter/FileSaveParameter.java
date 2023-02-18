package com.smart.file.core.parameter;

import com.smart.file.core.constants.FileTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;
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
public class FileSaveParameter extends FileSaveCommonParameter {

    /**
     * 文件存储器编码
     */
    @NonNull
    private String fileStorageCode;

    /**
     * 文件类型，默认NORMAL
     */
    @Nullable
    private FileTypeEnum type;
}
