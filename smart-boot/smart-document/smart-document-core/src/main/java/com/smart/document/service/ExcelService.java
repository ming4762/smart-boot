package com.smart.document.service;

import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ShiZhongMing
 * 2021/8/20 13:41
 * @since 1.0
 */
public interface ExcelService {

    /**
     * 填充excel
     * @param inputStream 模板输入流
     * @param outputStream 输出流
     * @param model 填充数据
     * @throws IOException IOException
     */
    void fillExcel(@NonNull InputStream inputStream, @NonNull OutputStream outputStream, Object model) throws IOException;

}
