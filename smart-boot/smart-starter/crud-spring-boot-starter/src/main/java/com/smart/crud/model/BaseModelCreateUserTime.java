package com.smart.crud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author jackson
 * 2020/1/22 2:17 下午
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseModelCreateUserTime extends BaseModel {

    private static final long serialVersionUID = 5157863351256809974L;
    protected Long createUserId;

    protected LocalDateTime createTime;

    protected String createBy;
}
