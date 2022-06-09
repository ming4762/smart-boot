package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.crud.json.LocaleJson;
import com.smart.crud.model.BaseModelCreateUserTime;
import com.smart.crud.mybatis.handler.LocaleTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.Locale;

/**
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
@TableName(value = "sys_i18n_item", autoResultMap = true)
@Getter
@Setter
public class SysI18nItemPO extends BaseModelCreateUserTime {
    @Serial
    private static final long serialVersionUID = -685851423581308459L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long i18nItemId;

    private Long i18nId;

    @TableField(typeHandler = LocaleTypeHandler.class)
    @JsonSerialize(using = LocaleJson.LocaleSerializer.class)
    @JsonDeserialize(using = LocaleJson.LocaleDeserializer.class)
    private Locale locale;

    private String value;

    private Boolean useYn;

    private Boolean deleteYn;
}
