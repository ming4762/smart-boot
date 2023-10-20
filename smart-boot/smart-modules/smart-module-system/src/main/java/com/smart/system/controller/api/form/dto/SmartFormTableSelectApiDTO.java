package com.smart.system.controller.api.form.dto;

import com.smart.crud.query.PageSortQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Smart-form组件 table-select api dto
 * @author zhongming4762
 * 2023/2/18 17:42
 */
@Schema(title = "Smart-form组件 table-select api")
@Getter
@Setter
@ToString
public class SmartFormTableSelectApiDTO implements Serializable {

    @Schema(title = "实体类名", example = "com.smart.system.model.SysUserPO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String modelClassName;

    @Schema(title = "value对应字段名称", example = "userId", requiredMode = Schema.RequiredMode.REQUIRED)
    private String valueFieldName;

    @Schema(title = "label对应字段名称", example = "username", requiredMode = Schema.RequiredMode.REQUIRED)
    private String labelFieldName;

    @Schema(title = "查询条件", requiredMode = Schema.RequiredMode.REQUIRED)
    private PageSortQuery queryParameter;
}
