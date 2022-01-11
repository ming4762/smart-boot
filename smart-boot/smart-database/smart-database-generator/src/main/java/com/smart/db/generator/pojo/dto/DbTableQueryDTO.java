package com.smart.db.generator.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author ShiZhongMing
 * 2021/4/30 14:47
 * @since 1.0
 */
@ApiModel("数据库表格查询")
@Getter
@Setter
@ToString
public class DbTableQueryDTO {

    @NotNull(message = "数据库连接ID不能为空")
    @ApiModelProperty("数据库连接ID")
    private Long dbConnectionId;

    @ApiModelProperty("数据库表名")
    @NotNull(message = "表名称不能为空")
    private String tableName;
}
