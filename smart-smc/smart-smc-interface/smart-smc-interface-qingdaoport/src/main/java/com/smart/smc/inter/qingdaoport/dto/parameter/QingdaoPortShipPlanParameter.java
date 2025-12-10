package com.smart.smc.inter.qingdaoport.dto.parameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 云港通-集装箱船舶计划查询参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 15:50
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "云港通-集装箱船舶计划查询参数")
public class QingdaoPortShipPlanParameter implements Serializable {

    @Schema(description = "船名代码")
    private String shipCode;

    @Schema(description = "船舶IMO")
    private String imo;

    @Schema(description = "中文船名")
    private String vesselNameCn;

    @Schema(description = "英文船名")
    private String vesselNameEn;

    @Schema(description = "进口航次")
    private String imVoyageNo;

    @Schema(description = "出口航次")
    private String exVoyageNo;
}
