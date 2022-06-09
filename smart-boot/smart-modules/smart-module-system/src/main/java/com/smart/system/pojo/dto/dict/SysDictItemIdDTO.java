package com.smart.system.pojo.dto.dict;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字典序ID
 * @author ShiZhongMing
 * 2022/2/8
 * @since 2.0.0
 */
@Getter
@Setter
@ToString
public class SysDictItemIdDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -378829530122590266L;
    private String dictCode;

    private String dictItemCode;
}
