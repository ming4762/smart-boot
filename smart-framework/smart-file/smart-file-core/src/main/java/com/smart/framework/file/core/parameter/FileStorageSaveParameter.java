package com.smart.framework.file.core.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.jspecify.annotations.Nullable;

import java.io.Serial;

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

    @Serial
    private static final long serialVersionUID = -7345281261754013964L;

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
