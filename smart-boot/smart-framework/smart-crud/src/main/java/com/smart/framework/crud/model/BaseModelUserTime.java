package com.smart.framework.crud.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 带有创建/修改用户信息的基础类
 * @author jackson
 * 2020/1/21 10:04 下午
 */
@Getter
@Setter
public abstract class BaseModelUserTime extends BaseModelCreateUserTime {
    @Serial
    private static final long serialVersionUID = 2257266050574705690L;

    @TableField(fill = FieldFill.UPDATE)
    protected Long updateUserId;

    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime updateTime;

    @TableField(fill = FieldFill.UPDATE)
    protected String updateBy;
}
