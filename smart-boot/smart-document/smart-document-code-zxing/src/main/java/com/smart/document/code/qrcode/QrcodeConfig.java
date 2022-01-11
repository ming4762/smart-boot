package com.smart.document.code.qrcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.smart.document.code.BaseCodeConfig;
import com.smart.document.code.constants.CodeConstants;
import com.smart.document.code.qrcode.eye.CodeEyeFormat;
import com.smart.document.code.qrcode.eye.CodeEyeFormatProvider;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 二维码配置类
 * @author ShiZhongMing
 * 2021/8/11 12:38
 * @since 1.0
 */
@Getter
@Setter
public class QrcodeConfig extends BaseCodeConfig {

    private static final long serialVersionUID = -7331090318277237986L;

    private final ConcurrentHashMap<EncodeHintType, Serializable> hints;

    /**
     * 码眼的默认颜色
     */
    private String codeEyesBorderColor = CodeConstants.DEFAULT_CODE_MASTER_COLOR;
    private String codeEyesPointColor = CodeConstants.DEFAULT_CODE_MASTER_COLOR;
    /**
     * 码眼类型
     */
    private CodeEyeFormatProvider codeEyeFormat = CodeEyeFormat.R_BORDER_R_POINT;

    public QrcodeConfig() {
        this(CodeConstants.Qrcode.DEFAULT_CODE_WIDTH, CodeConstants.Qrcode.DEFAULT_CODE_HEIGHT);
    }

    public QrcodeConfig(int width, int height) {
        super(width, height);
        hints = new ConcurrentHashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, CodeConstants.Qrcode.DEFAULT_CODE_PADDING);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
    }

}
