package com.smart.message.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.message.core.constants.SmartMessageChannelType1Enum;
import com.message.core.constants.SmartMessageChannelType2Enum;
import com.smart.crud.annotation.TableUseYnField;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* smart_message_channel - 消息通道信息
* @author SmartCodeGenerator
* 2024年5月17日 下午5:13:58
*/
@Getter
@Setter
@TableName("smart_message_channel")
public class SmartMessageChannelPO extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = 3295680792212872711L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * channel_code - 通道编码
    */
    private String channelCode;

    /**
    * channel_name - 通道名称
    */
    private String channelName;

    /**
    * channel_type1 - 一级通道类型
    */
    private SmartMessageChannelType1Enum channelType1;

    /**
    * channel_type2 - 二级通道类型
    */
    private SmartMessageChannelType2Enum channelType2;

    /**
    * channel_properties - 通道参数
    */
    private String channelProperties;

    /**
     * 是否系统内置
     */
    private Boolean builtInYn;

    /**
    * use_yn - useYn
    */
    @TableUseYnField
    private Boolean useYn;

    /**
    * seq - seq
    */
    private Integer seq;

    /**
    * remark - remark
    */
    private String remark;

}