package com.smart.db.generator.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 保存模板对应的用户组
 * @author ShiZhongMing
 * 2021/6/16 9:24
 * @since 1.0
 */
@ToString
@Getter
@Setter
public class DbTemplateUserGroupSaveDTO {

    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    @NotNull(message = "用户组ID不能为空")
    private List<Long> groupIdList;
}
