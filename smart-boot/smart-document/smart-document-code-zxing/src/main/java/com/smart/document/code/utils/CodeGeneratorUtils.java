package com.smart.document.code.utils;

import com.google.zxing.BarcodeFormat;
import com.smart.document.code.barcode.BarcodeConfig;
import com.smart.document.code.barcode.BarcodeGenerator;
import com.smart.document.code.barcode.SimpleBarcodeGenerator;
import com.smart.document.code.qrcode.QrcodeConfig;
import com.smart.document.code.qrcode.QrcodeGenerator;
import com.smart.document.code.qrcode.SimpleQrcodeGenerator;
import com.smart.document.model.code.BarcodeGeneratorData;
import com.smart.document.model.code.QrcodeGeneratorData;
import lombok.SneakyThrows;

import java.io.OutputStream;

/**
 * @author ShiZhongMing
 * 2021/8/13 21:41
 * @since 1.0
 */
public class CodeGeneratorUtils {

    private CodeGeneratorUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 创建条形码
     * @param parameter 条形码参数
     * @param outputStream 输出流
     */
    @SneakyThrows
    public static void generateBarcode(BarcodeGeneratorData parameter, OutputStream outputStream) {
        BarcodeConfig barcodeConfig = new BarcodeConfig();
        if (parameter.getFormat() != null) {
            barcodeConfig.setFormat(BarcodeFormat.valueOf(parameter.getFormat().name()));
        }
        if (parameter.getHeight() != null) {
            barcodeConfig.setHeight(parameter.getHeight());
        }
        if (parameter.getWidth() != null) {
            barcodeConfig.setWidth(parameter.getWidth());
        }
        if (parameter.getShowWord() != null) {
            barcodeConfig.setShowWord(parameter.getShowWord());
        }
        if (parameter.getWordHeight() != null) {
            barcodeConfig.setWordHeight(parameter.getWordHeight());
        }
        if (parameter.getMargin() != null) {
            barcodeConfig.setMargin(parameter.getMargin());
        }
        if (parameter.getWordTopMargin() != null) {
            barcodeConfig.setWordTopMargin(parameter.getWordTopMargin());
        }
        // 字体
        if (parameter.getFont() != null) {
            barcodeConfig.setWordFont(parameter.getFont().createFont());
            if (parameter.getFont().getAutoSpace() != null) {
                barcodeConfig.setAutoSpace(parameter.getFont().getAutoSpace());
            }
        }
        try (
                BarcodeGenerator generator = new SimpleBarcodeGenerator(barcodeConfig)
                ) {
            generator.generate(parameter.getContent())
                    .toStream(outputStream);
        }
    }

    /**
     * 生成二维码
     * @param parameter 参数
     * @param outputStream 输出流
     */
    @SneakyThrows
    public static void generateQrcode(QrcodeGeneratorData parameter, OutputStream outputStream) {
        QrcodeConfig config = new QrcodeConfig();
        if (parameter.getHeight() != null) {
            config.setHeight(parameter.getHeight());
        }
        if (parameter.getWidth() != null) {
            config.setWidth(parameter.getWidth());
        }
        if (parameter.getMargin() != null) {
            config.setMargin(parameter.getMargin());
        }
        try (QrcodeGenerator qrcodeGenerator = new SimpleQrcodeGenerator(config)) {
            qrcodeGenerator.generate(parameter.getContent())
                    .toStream(outputStream);
        }
    }
}
