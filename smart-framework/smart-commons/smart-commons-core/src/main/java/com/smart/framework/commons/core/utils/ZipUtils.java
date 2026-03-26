package com.smart.framework.commons.core.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 * @author shizhongming
 * 2025/2/14 14:44
 * @since 5.0.0
 */
public class ZipUtils {

    private ZipUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 压缩文件
     * @param outputStream 输出流
     * @param fileNameList 文件名列表
     * @param inputStreamList 输入流列表
     */
    @SneakyThrows(IOException.class)
    public static void zip(@NonNull OutputStream outputStream, @NonNull List<String> fileNameList, @NonNull List<InputStream> inputStreamList) {
        if (fileNameList.size() != inputStreamList.size()) {
            throw new IllegalArgumentException("fileNameList size must equal inputStreamList size");
        }
        if (fileNameList.isEmpty()) {
            throw new IllegalArgumentException("fileNameList must not null");
        }
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8)) {
            for (int i = 0; i < fileNameList.size(); i++) {
                ZipEntry zipEntry = new ZipEntry(fileNameList.get(i));
                zipOutputStream.putNextEntry(zipEntry);

                IOUtils.copy(inputStreamList.get(i), zipOutputStream);
                zipOutputStream.closeEntry();
            }
        }
    }

}
