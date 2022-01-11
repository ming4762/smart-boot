package com.smart.document.code.barcode;

import lombok.NonNull;

/**
 * 条形码生成工具
 * @author ShiZhongMing
 * 2021/8/12 21:33
 * @since 1.0
 */
public class SimpleBarcodeGenerator extends AbstractBarcodeGenerator implements BarcodeGenerator {

    public SimpleBarcodeGenerator() {
        super();
    }

    public SimpleBarcodeGenerator(@NonNull BarcodeConfig config) {
        super(config);
    }
}
