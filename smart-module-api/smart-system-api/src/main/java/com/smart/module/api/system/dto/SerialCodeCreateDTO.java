package com.smart.module.api.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 业务编码查询结果
 * @author zhongming4762
 * 2023/6/2
 */
@Getter
@Setter
@ToString
public class SerialCodeCreateDTO implements Serializable {

    private Long id;

    /**
     * code - 编号
     */
    private String code;

    /**
     * name - 名称
     */
    private String name;

    /**
     * prefix - 编码前缀
     */
    private String prefix;

    /**
     * date_format - 日期格式
     */
    private String dateFormat;

    /**
     * serial_length - 流水长度
     */
    private Integer serialLength;

    /**
     * min_value - 最小值
     */
    private Long minValue;

    /**
     * max_value - 最大值，-1不限制
     */
    private Long maxValue;

    /**
     * step_value - 步长
     */
    private Integer stepValue;

    /**
     * current_value - 当前值
     */
    private Long currentValue;

    /**
     * 编码列表
     */
    private List<String> serialList;
}
