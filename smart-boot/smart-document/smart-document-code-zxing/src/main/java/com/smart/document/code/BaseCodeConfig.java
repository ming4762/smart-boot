package com.smart.document.code;

import com.smart.document.code.constants.CodeConstants;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * barcode 基础配置
 * @author ShiZhongMing
 * 2021/8/11 11:21
 * @since 1.0
 */
@Getter
@Setter
public class BaseCodeConfig implements Serializable {
    private static final long serialVersionUID = 4769100352058779401L;

    private int width;

    private int height;

    private String masterColor = CodeConstants.DEFAULT_CODE_MASTER_COLOR;

    private String slaveColor = CodeConstants.DEFAULT_CODE_SLAVE_COLOR;

    private int borderRadius = CodeConstants.DEFAULT_CODE_BORDER_RADIUS;

    String borderColor = CodeConstants.DEFAULT_CODE_BORDER_COLOR;

    private CodeConstants.BorderStyle borderStyle = CodeConstants.BorderStyle.DASHED;

    private int borderDashGranularity = CodeConstants.DEFAULT_CODE_BORDER_DASH_GRANULARITY;

    private int margin = CodeConstants.DEFAULT_CODE_MARGIN;

    private int borderSize = CodeConstants.DEFAULT_CODE_BORDER_SIZE;


    public BaseCodeConfig(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public BaseCodeConfig(int width, int height, String masterColor, String slaveColor) {
        this(width, height);
        this.masterColor = masterColor;
        this.slaveColor = slaveColor;
    }
}
