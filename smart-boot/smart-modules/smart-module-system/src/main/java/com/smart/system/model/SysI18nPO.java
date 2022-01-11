package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
@TableName("sys_i18n")
@Getter
@Setter
public class SysI18nPO extends BaseModelUserTime {
    private static final long serialVersionUID = 1567001566507494995L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long i18nId;

    private String platform;

    private String i18nCode;

    /**
     * 备注
     */
    private String remark;

    private Long groupId;

    private Boolean useYn;

    private Boolean deleteYn;

    private Integer seq;
}
