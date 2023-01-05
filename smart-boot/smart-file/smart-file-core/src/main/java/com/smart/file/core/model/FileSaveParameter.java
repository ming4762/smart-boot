package com.smart.file.core.model;

import lombok.*;
import org.springframework.lang.NonNull;

/**
 * 文件存储参数
 * @author zhongming4762
 * 2023/1/3 21:24
 */
@Getter
@Setter
public class FileSaveParameter {

    /**
     * 文件名
     */
    @NonNull
    String filename;

    /**
     * 文件路径
     * 无路径设为path
     */
    @NonNull
    String path;

    private FileSaveParameter(String filename, String path) {
        this.filename = filename;
        this.path = path;
    }

    public static FileSaveParameter create(String filename, String path) {
        return new FileSaveParameter(filename, path);
    }

    public static FileSaveParameter create(String filename) {
        return create(filename, "");
    }
}
