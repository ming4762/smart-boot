package com.smart.module.document.service;

import com.smart.module.document.pojo.dto.excel.ExcelFillWithCodeData;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/8/27 13:48
 * @since 1.0
 */
public interface DocumentExcelService {

    /**
     * 填充单sheet excel，带有条形码/二维码
     * @param inputStream 输入流
     * @param outputStream 输出流
     * @param data 数据
     */
    void fillSingleWithCode(InputStream inputStream, OutputStream outputStream, ExcelFillWithCodeData data);

    /**
     * 填充多sheet excel，带有条形码/二维码
     * @param inputStream 输入流
     * @param outputStream 输出流
     * @param dataList 数据列表
     * @param sheetNameList sheet名字列表
     */
    void fillMultiWithCode(InputStream inputStream, OutputStream outputStream, List<ExcelFillWithCodeData> dataList, List<String> sheetNameList);
}
