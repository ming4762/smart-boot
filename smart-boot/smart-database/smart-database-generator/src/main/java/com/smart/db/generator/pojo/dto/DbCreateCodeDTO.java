package com.smart.db.generator.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/5/8 9:47
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbCreateCodeDTO implements Serializable {
    private static final long serialVersionUID = 525969661071146013L;

    @NotNull(message = "主ID不能为空")
    private Long mainId;

    @NotNull(message = "类名不能为空")
    private String className;

    private String description;

    @NotNull(message = "包名不能为空")
    private String packages;

    @NotEmpty(message = "模板不能为空")
    private List<Long> templateIdList;

    private String controllerBasePath;

    /**
     * ext包名
     */
    private String extPackages;
}
