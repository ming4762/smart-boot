package com.smart.file.core.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * 文件存储参数
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
@ToString
@SuperBuilder
public class FileSaveCommonParameter implements Serializable {

    /**
     * 文件名，默认为文件实际名字
     */
    @Nullable
    private String filename;

    /**
     * 文件存储目录
     */
    @Nullable
    private String folder;

    public String getFolder() {
        return this.folder == null ? "" : this.folder;
    }

}
