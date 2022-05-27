package com.smart.db.generator.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
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
public class DbTemplateUserGroupSaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4778435410342902221L;
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    @NotNull(message = "用户组ID不能为空")
    private List<Long> groupIdList;
}
