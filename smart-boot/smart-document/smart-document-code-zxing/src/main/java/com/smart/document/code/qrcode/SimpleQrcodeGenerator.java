package com.smart.document.code.qrcode;

import lombok.NonNull;

/**
 * 简单的二维码生成工具
 * @author Bosco.Liao
 * @author ShiZhongMing
 * 2021/8/11 13:13
 * @since 1.0
 */
public class SimpleQrcodeGenerator extends AbstractQrcodeGenerator implements QrcodeGenerator {


    public SimpleQrcodeGenerator() {
        super();
    }

    public SimpleQrcodeGenerator(@NonNull QrcodeConfig config) {
        super(config);
    }
}
