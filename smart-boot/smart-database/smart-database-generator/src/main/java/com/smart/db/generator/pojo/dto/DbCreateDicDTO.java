package com.smart.db.generator.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author ShiZhongMing
 * 2021/7/30 20:27
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbCreateDicDTO {

    @NotNull(message = "数据库连接不能为空")
    private Long connectionId;

    @NotNull(message = "请选择数据库字典模板")
    private Long templateId;
}
