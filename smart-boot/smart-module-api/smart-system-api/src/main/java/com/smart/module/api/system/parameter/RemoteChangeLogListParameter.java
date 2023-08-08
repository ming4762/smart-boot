package com.smart.module.api.system.parameter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 修改记录查询参数
 * @author zhongming4762
 * 2023/8/1 9:51
 */
@Getter
@Setter
@Schema(description = "修改记录查询参数")
public class RemoteChangeLogListParameter implements Serializable {

    @Schema(description = "标识位")
    @NotBlank(message = "标识位不能为空")
    private String ident;

    @Schema(description = "业务ID")
    private Long businessId;
}
