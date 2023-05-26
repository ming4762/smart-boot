package com.smart.sms.manager.pojo.parameter;

import com.smart.sms.core.constants.SmartSmsChannelEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* smart_sms_channel_manager - 短信通道表
* @author SmartCodeGenerator
* 2023年5月26日 上午9:34:41
*/
@Getter
@Setter
@ToString
public class SmartSmsChannelManagerSaveUpdateParameter implements Serializable {


    /**
    * 
    */
    private Long id;
    /**
    * 通道编号，唯一
    */
    @NotNull(message = "通道编号不能为空")
    private String channelCode;
    /**
    * 通道名称
    */
    @NotNull(message = "通道名称不能为空")
    private String channelName;
    /**
    * 通道类型
    */
    @NotNull(message = "通道类型不能为空")
    private SmartSmsChannelEnum channelType;
    /**
    * 是否是默认的，只能有一个默认的
    */
    @NotNull(message = "是否是默认的不能为空")
    private Boolean isDefault;
    /**
    * 
    */
    @NotNull(message = "需要不能为空")
    private Integer seq;
    /**
    * 通道参数
    */
    @NotNull(message = "通道参数不能为空")
    private String channelProperties;
    /**
    * 
    */
    private String remark;
    /**
    * 
    */
    private Boolean useYn;

}