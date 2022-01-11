package com.smart.document.code.constants;

import com.google.zxing.BarcodeFormat;

/**
 * @author ShiZhongMing
 * 2021/8/11 12:35
 * @since 1.0
 */
public interface CodeConstants {

    String DEFAULT_CODE_MASTER_COLOR = "#000000";

    String DEFAULT_CODE_SLAVE_COLOR = "#FFFFFF";

    String DEFAULT_CODE_BORDER_COLOR = "#808080";

    String IMAGE_TYPE = "PNG";

    int DEFAULT_CODE_BORDER_RADIUS = 0;

    int DEFAULT_CODE_BORDER_DASH_GRANULARITY = 5;

    int DEFAULT_CODE_MARGIN = 10;

    int DEFAULT_CODE_BORDER_SIZE = 0;

    /**
     * 条形码配置
     */
    interface Barcode {
        /**
         * 条形码默认宽度高度
         */
        int DEFAULT_CODE_WIDTH = 200;
        int DEFAULT_CODE_HEIGHT = 100;

        /**
         * 文字高度
         */
        int DEFAULT_WORD_HEIGHT = 25;

        int DEFAULT_WORD_TOP_MARGIN = 14;

        /**
         * 条码默认格式
         */
        BarcodeFormat DEFAULT_BARCODE_FORMAT = BarcodeFormat.CODE_39;
    }

    /**
     * 二维码配置
     */
    interface Qrcode {
        /**
         * 二维码默认宽度高度
         */
        int DEFAULT_CODE_WIDTH = 250;
        int DEFAULT_CODE_HEIGHT = 250;
        int DEFAULT_CODE_PADDING = 0;
    }

    /**
     * 二维码border类型
     */
    enum BorderStyle {
        /**
         * 实线
         */
        SOLID,
        /**
         * 虚线
         */
        DASHED
    }
}
