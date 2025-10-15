package com.smart.framework.commons.pdf.data;

import lombok.*;

import java.io.Serializable;

/**
 * PDF签名数据
 * @author shizhongming
 * 2025/10/15 14:36
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfSignatureData implements Serializable {

     /**
     * 签名人名称
     */
    private String signerName;

     /**
     * 签名原因
     */
    private String reason;

     /**
     * 签名位置
     */
    private String location;
}
