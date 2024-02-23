package com.smart.crud.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字符串查询条件
 * @author shizhongming
 * 2024/2/22 14:47
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class StringParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = 6155240649882234775L;
    private String value;
}
