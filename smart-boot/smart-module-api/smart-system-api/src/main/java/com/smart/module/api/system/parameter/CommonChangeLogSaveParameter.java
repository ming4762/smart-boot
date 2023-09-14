package com.smart.module.api.system.parameter;

import com.smart.module.api.system.constants.SmartChangeLogEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/8/1 11:13
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonChangeLogSaveParameter implements Serializable {

    @NotBlank(message = "标识不能为空")
    @Schema(description = "标识位，可以用表名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ident;

    @Schema(description = "变更记录保存参数")
    private Long businessId;

    @Schema(description = "变更记录保存参数")
    private String businessData;

    @Schema(description = "操作类型，为null则自动判断类型")
    private SmartChangeLogEnum operateType;

    /**
     * 是否忽略 修改为null的 值
     */
    @Schema(description = "是否忽略 修改为null的 值")
    private Boolean ignoreAfterNull;

    @Schema(description = "是否保留新增记录详情")
    private Boolean saveCreateDetail;

    /**
     * 预留参数
     */
    @Schema(description = "预留参数")
    private String params1;
    private String params2;

    private String params3;
}