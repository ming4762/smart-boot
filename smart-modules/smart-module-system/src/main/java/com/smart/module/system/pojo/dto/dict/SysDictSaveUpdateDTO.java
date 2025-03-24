package com.smart.module.system.pojo.dto.dict;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/4/17 9:35
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysDictSaveUpdateDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2384876258294207222L;

    private Long id;

    /**
     * dict_code - 字典编码
     */
    @NotNull(message = "字典编码不能为空")
    private String dictCode;

    /**
     * dict_name - 字典名称
     */
    @NotNull(message = "字典名称不能为空")
    private String dictName;

    /**
     * seq - 序号
     */
    private Integer seq;

    private String remark;

    /**
     * use_yn - 启用状态
     */
    private Boolean useYn;

    /**
     * 是否平台通用
     */
    private Boolean tenantCommonYn;

}
