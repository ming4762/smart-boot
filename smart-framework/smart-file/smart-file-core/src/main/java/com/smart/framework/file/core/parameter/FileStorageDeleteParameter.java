package com.smart.framework.file.core.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文件删除参数
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
@SuperBuilder
public class FileStorageDeleteParameter extends FileStorageCommonParameter {

    @Serial
    private static final long serialVersionUID = -2606746219743483001L;

    private List<FileStorageDeleteItem> fileStoreList;

    @Getter
    @AllArgsConstructor
    public static class FileStorageDeleteItem implements Serializable {
        private String fileStoreKey;
        private boolean encryptedYn;
    }
}
