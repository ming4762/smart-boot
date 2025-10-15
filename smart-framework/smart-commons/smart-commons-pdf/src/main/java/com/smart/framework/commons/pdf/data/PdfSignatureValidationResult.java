package com.smart.framework.commons.pdf.data;

import lombok.*;

import java.time.ZonedDateTime;

/**
 * PDF 签名验证结果
 * @author shizhongming
 * 2025/10/15 17:18
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfSignatureValidationResult {

    /**
     * 签名名称
     */
    private String signatureName;
    /**
     * 签名日期
     */
    private ZonedDateTime signDate;
    /**
     * 签名算法
     */
    private String subFilter;
    /**
     * 签名原因
     */
    private String reason;
    /**
     * 签名位置
     */
    private String location;
    /**
     * 是否有效
     */
    private boolean isValid;
}
