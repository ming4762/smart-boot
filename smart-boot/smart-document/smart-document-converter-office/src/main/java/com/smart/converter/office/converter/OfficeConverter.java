package com.smart.converter.office.converter;

import lombok.NonNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author ShiZhongMing
 * 2021/8/25 15:35
 * @since 1.0
 */
public interface OfficeConverter extends Closeable {

    /**
     * 转换的类型
     * @param fileType 文件类型
     * @return 转换的文件类型
     */
    OfficeConverter toFormat(@NonNull ConvertFileType fileType);

    /**
     * 执行保存路径
     * @param path 文件路径
     */
    void to(String path);

    /**
     * 指定保存文件
     * @param file 保存的文件
     */
    void to(File file);

    /**
     * 指定输出流
     * @param outputStream 输出流
     * @throws IOException IOException
     */
    void to(OutputStream outputStream) throws IOException;
}
