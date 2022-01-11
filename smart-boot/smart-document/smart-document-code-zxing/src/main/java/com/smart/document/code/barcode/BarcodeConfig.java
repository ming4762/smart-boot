package com.smart.document.code.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.smart.document.code.BaseCodeConfig;
import com.smart.document.code.constants.CodeConstants;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 条形码配置类
 * @author ShiZhongMing
 * 2021/8/12 20:45
 * @since 1.0
 */
@Getter
@Setter
public class BarcodeConfig extends BaseCodeConfig {
    private static final long serialVersionUID = -528806135748042963L;

    private final ConcurrentHashMap<EncodeHintType, Serializable> hints;
    /**
     * 条码格式
     */
    private BarcodeFormat format = CodeConstants.Barcode.DEFAULT_BARCODE_FORMAT;

    /**
     * 文字字体
     */
    private Font wordFont = new Font("微软雅黑", Font.PLAIN, 18);

    private int wordTopMargin = CodeConstants.Barcode.DEFAULT_WORD_TOP_MARGIN;

    /**
     * 文字是否自动空格
     */
    private Boolean autoSpace = true;

    /**
     * 文字高度
     */
    private int wordHeight = CodeConstants.Barcode.DEFAULT_WORD_HEIGHT;

    private boolean showWord = true;

    public BarcodeConfig() {
        this(CodeConstants.Barcode.DEFAULT_CODE_WIDTH, CodeConstants.Barcode.DEFAULT_CODE_HEIGHT);
    }

    public BarcodeConfig(int width, int height) {
        super(width, height);
        this.hints = new ConcurrentHashMap<>();
        this.hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        this.hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
    }

}
