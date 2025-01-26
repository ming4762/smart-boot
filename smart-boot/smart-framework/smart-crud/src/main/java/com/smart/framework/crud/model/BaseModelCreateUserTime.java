package com.smart.framework.crud.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.time.ZonedDateTime;

/**
 * @author jackson
 * 2020/1/22 2:17 下午
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseModelCreateUserTime extends BaseModel {

    @Serial
    private static final long serialVersionUID = 5157863351256809974L;

    @TableField(fill = FieldFill.INSERT)
    protected Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    protected ZonedDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    protected String createBy;
}
