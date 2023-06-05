package com.smart.module.api.system.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 业务编码查询参数
 * @author zhongming4762
 * 2023/6/2
 */
@Getter
@Setter
@ToString
public class SerialCodeCreateParameter implements Serializable {

    private String code;

    private Integer number;
}
