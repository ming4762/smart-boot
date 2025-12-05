package com.smart.framework.commons.pdf.data;

import lombok.*;

import java.io.InputStream;

/**
 * PDF 签名图片数据
 * @author shizhongming
 * 2025/10/15 16:37
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfSignatureImageData {

    @NonNull
    private InputStream imageStream;

    /**
     * 签章页面 -1 表示所有页面
     */
    @Builder.Default
    private int page = -1;

     /**
      * 签章位置 x 坐标
      */
    @Builder.Default
    private float x = 100;

     /**
      * 签章位置 y 坐标
      */
    @Builder.Default
    private float y = 100;

     /**
      * 签章图片宽度
      */
    @Builder.Default
    private float width = 100;

     /**
      * 签章图片高度
      */
    @Builder.Default
    private float height = 100;
}
