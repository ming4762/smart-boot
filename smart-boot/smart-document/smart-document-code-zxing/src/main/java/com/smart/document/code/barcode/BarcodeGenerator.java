package com.smart.document.code.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.smart.document.code.Generator;
import lombok.NonNull;

/**
 * 二维码生成接口
 * @author ShiZhongMing
 * 2021/8/12 21:22
 * @since 1.0
 */
public interface BarcodeGenerator extends Generator {

    /**
     * 设置格式
     * @param format 格式
     * @return this
     */
    BarcodeGenerator format(BarcodeFormat format);

    /**
     * 执行生成操作
     * @param content 内容
     * @param format 格式
     * @return this
     * @throws WriterException WriterException
     */
    BarcodeGenerator generate(@NonNull String content, BarcodeFormat format) throws WriterException;
}
