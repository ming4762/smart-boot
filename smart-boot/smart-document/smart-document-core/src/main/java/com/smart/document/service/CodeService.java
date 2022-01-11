package com.smart.document.service;

import com.smart.document.model.code.BarcodeGeneratorData;
import com.smart.document.model.code.QrcodeGeneratorData;

import java.io.OutputStream;

/**
 * 条形码/二维码服务
 * @author ShiZhongMing
 * 2021/8/20 13:08
 * @since 1.0
 */
public interface CodeService {

    /**
     * 生成条形码
     * @param data 条形码数据
     * @param outputStream 输出流
     */
    void generateBarcode(BarcodeGeneratorData data, OutputStream outputStream);

    /**
     * 生成二维码
     * @param data 二维码生成参数
     * @param outputStream 输出流
     */
    void generateQrcode(QrcodeGeneratorData data, OutputStream outputStream);
}
