package com.smart.smc.inter.qingdaoport.dto.parameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 *
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 16:20
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "云港通-智能转运平台-查询IMO号参数")
public class QingdaoPortTransferImoQueryParameter implements Serializable {

    /**
     * 中文船名
     */
    @Schema(description = "中文船名")
    private String zwcm;

    /**
     * 英文船名
     */
    @Schema(description = "英文船名")
    private String ywcm;

    /**
     * 船名代码
     */
    @Schema(description = "船名代码")
    private String cmdm;
}
