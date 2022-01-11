package com.smart.document.service;

import com.smart.document.constants.DocumentFormatEnum;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文档转换服务
 * @author ShiZhongMing
 * 2021/8/24 15:17
 * @since 1.0
 */
public interface DocumentConverterService {

    /**
     * 转换文档
     * @param inputStream 文档输入流
     * @param outputStream 文档输出流
     * @param fromFormat 源格式
     * @param toFormat 输出格式
     */
    void convert(InputStream inputStream, OutputStream outputStream, DocumentFormatEnum fromFormat, DocumentFormatEnum toFormat);
}
