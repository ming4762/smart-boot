package com.smart.converter.office.converter;

import com.jacob.com.ComThread;
import com.smart.commons.core.exception.SystemException;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author ShiZhongMing
 * 2021/8/25 15:47
 * @since 1.0
 */
public abstract class AbstractOfficeConverter implements OfficeConverter {

    protected String fromPath;

    protected ConvertFileType fileType;

    protected String toPath;


    protected AbstractOfficeConverter(@NonNull String fromPath) {
        this.fromPath = fromPath;
    }

    @Override
    public OfficeConverter toFormat(@NonNull ConvertFileType fileType) {
        this.fileType = fileType;
        return this;
    }

    @Override
    public void to(String path) {
        this.toPath = path;
        this.execute();
    }

    @Override
    public void to(File file) {
        this.toPath = file.getAbsolutePath();
        this.execute();
    }

    @Override
    public void to(OutputStream outputStream) throws IOException {
        // 创建临时目录
        Path path = Files.createTempDirectory("office");
        this.toPath = path.toAbsolutePath() + File.separator + UUID.randomUUID() + this.getExtension();
        this.execute();
        try (FileInputStream inputStream = new FileInputStream(this.toPath)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    protected String getExtension() {
        if (StringUtils.isBlank(this.fileType.getExtension())) {
            return ".tmp";
        }
        return "." + this.fileType.getExtension();
    }

    @SneakyThrows(IOException.class)
    private void execute() {
        if (StringUtils.isBlank(this.fromPath)) {
            throw new IllegalArgumentException("convert file is null, please set fromPath");
        }
        // 创建目录和文件
        File toFile = new File(toPath);
        // 获取文件目录
        File dirFile = toFile.getParentFile();
        if (!dirFile.exists()) {
            boolean result = dirFile.mkdirs();
            if (!result) {
                throw new SystemException("directory create fail");
            }
        }
        // 文件已存在，删除
        if (toFile.exists()) {
            Files.delete(toFile.toPath());
        }
        ComThread.InitSTA();
        this.doExecute();
    }

    @Override
    public void close() {
        ComThread.Release();
    }

    /**
     * 执行转换
     */
    protected abstract void doExecute();
}
