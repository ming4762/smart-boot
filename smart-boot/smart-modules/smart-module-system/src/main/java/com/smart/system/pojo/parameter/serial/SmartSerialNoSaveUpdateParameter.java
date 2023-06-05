package com.smart.system.pojo.parameter.serial;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* smart_serial_no - 业务编码表
* @author SmartCodeGenerator
* 2023年6月2日 上午9:56:44
*/
@Getter
@Setter
@ToString
public class SmartSerialNoSaveUpdateParameter implements Serializable {


    /**
    * 
    */
    private Long id;
    /**
    * 编号
    */
    private String code;
    /**
    * 名称
    */
    private String name;
    /**
    * 备注
    */
    private String remark;
    /**
    * 
    */
    private Integer seq;
    /**
    * 编码前缀
    */
    private String prefix;
    /**
    * 日期格式
    */
    private String dateFormat;
    /**
    * 流水长度
    */
    private Integer serialLength;
    /**
    * 最小值
    */
    private Long minValue;
    /**
    * 最大值，-1不限制
    */
    private Long maxValue;
    /**
    * 步长
    */
    private Integer stepValue;
    /**
    * 当前值
    */
    private Long currentValue;
    /**
    * 
    */
    private Boolean useYn;

}