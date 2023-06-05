package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
* smart_serial_no - 业务编码表
* @author SmartCodeGenerator
* 2023年6月2日 上午9:56:44
*/
@Getter
@Setter
@TableName("smart_serial_no")
public class SmartSerialNoPO extends BaseModelUserTime {

    @TableId(type = IdType.ASSIGN_ID)
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
    * remark - 备注
    */
    private String remark;

    /**
    * seq - seq
    */
    private Integer seq;

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
     * 当前日期，标记重置当前值
     */
    private LocalDate lastCurrentDate;

    /**
    * current_value - 当前值
    */
    private Long currentValue;

    /**
     * 填充字符
     */
    private String fill;

    /**
     * 编码格式
     *
     */
    private String serialFormat;


    /**
    * use_yn - useYn
    */
    private Boolean useYn;

    @TableLogic
    private Boolean deleteYn;

}