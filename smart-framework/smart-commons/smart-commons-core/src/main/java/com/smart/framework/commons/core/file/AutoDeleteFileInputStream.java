package com.smart.framework.commons.core.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 自动删除临时文件的 FileInputStream
 * 在流关闭时自动删除对应的临时文件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-21 21:13
 * @since 5.0.0
 */
public class AutoDeleteFileInputStream extends FileInputStream {

    private final File file;
    private boolean closed = false;

    public AutoDeleteFileInputStream(File file) throws IOException {
        super(file);
        this.file = file;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (!closed) {
                closed = true;
                if (file.exists() && !file.delete()) {
                    // 删除失败时标记为 JVM 退出时删除
                    file.deleteOnExit();
                }
            }
        }
    }
}
