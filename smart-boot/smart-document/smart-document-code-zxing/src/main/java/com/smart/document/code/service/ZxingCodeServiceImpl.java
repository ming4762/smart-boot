package com.smart.document.code.service;

import com.smart.document.code.utils.CodeGeneratorUtils;
import com.smart.document.model.code.BarcodeGeneratorData;
import com.smart.document.model.code.QrcodeGeneratorData;
import com.smart.document.service.CodeService;

import java.io.OutputStream;

/**
 * 二维码/条形码生成工具
 * @author ShiZhongMing
 * 2021/8/20 13:22
 * @since 1.0
 */
public class ZxingCodeServiceImpl implements CodeService {
    @Override
    public void generateBarcode(BarcodeGeneratorData data, OutputStream outputStream) {
        CodeGeneratorUtils.generateBarcode(data, outputStream);
    }

    @Override
    public void generateQrcode(QrcodeGeneratorData data, OutputStream outputStream) {
        CodeGeneratorUtils.generateQrcode(data, outputStream);
    }
}
