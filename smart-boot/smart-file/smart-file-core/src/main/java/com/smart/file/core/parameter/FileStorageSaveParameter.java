package com.smart.file.core.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

/**
 * 文件保存参数
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
@ToString
@SuperBuilder
public class FileStorageSaveParameter extends FileStorageCommonParameter {

    /**
     * 文件存储器ID
     */
    private Long fileStorageId;

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

    /**
     * 是否使用原始文件名
     */
    private boolean useOriginalFilename;

    public String getFolder() {
        return this.folder == null ? "" : this.folder;
    }

}
