package com.smart.sms.manager.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import com.smart.sms.core.constants.SmartSmsChannelEnum;
import lombok.Getter;
import lombok.Setter;

/**
* smart_sms_channel_manager - 短信通道表
* @author SmartCodeGenerator
* 2023年5月26日 上午9:34:41
*/
@Getter
@Setter
@TableName("smart_sms_channel_manager")
public class SmartSmsChannelManagerPO extends BaseModelUserTime {

    /**
    * id - id
    */
    private Long id;

    /**
    * channel_code - 通道编号，唯一
    */
    private String channelCode;

    /**
    * channel_name - 通道名称
    */
    private String channelName;

    /**
    * channel_type - 通道类型
    */
    private SmartSmsChannelEnum channelType;

    /**
    * is_default - 是否是默认的，只能有一个默认的
    */
    private Boolean isDefault;

    /**
    * channel_properties - 通道参数
    */
    private String channelProperties;

    /**
    * seq - seq
    */
    private Integer seq;

    /**
    * remark - remark
    */
    private String remark;

    /**
    * use_yn - useYn
    */
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;

}