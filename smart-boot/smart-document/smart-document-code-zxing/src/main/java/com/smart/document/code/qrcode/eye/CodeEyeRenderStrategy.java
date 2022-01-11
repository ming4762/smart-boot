package com.smart.document.code.qrcode.eye;

import lombok.Getter;

/**
 * 码眼类型
 * @author ShiZhongMing
 * 2021/8/11 15:39
 * @since 1.0
 */
@Getter
public enum CodeEyeRenderStrategy {
    /**
     * 码眼类型
     */
    POINT(2, 5),
    POINT_BORDER(0, 7);

    private final int start;

    private final int end;

    CodeEyeRenderStrategy(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
