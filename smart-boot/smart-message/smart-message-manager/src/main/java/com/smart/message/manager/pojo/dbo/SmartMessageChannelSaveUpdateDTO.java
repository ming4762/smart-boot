package com.smart.message.manager.pojo.dbo;

import com.message.core.constants.SmartMessageChannelType1Enum;
import com.message.core.constants.SmartMessageChannelType2Enum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
* smart_message_channel - 消息通道信息
* @author SmartCodeGenerator
* 2024年5月17日 下午5:13:58
*/
@Getter
@Setter
@ToString
public class SmartMessageChannelSaveUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6780019565174465595L;
    /**
    * id
    */
    private Long id;
    /**
    * 通道编码
    */
    @NotNull(message = "通道编码不能为空")
    private String channelCode;
    /**
    * 通道名称
    */
    @NotNull(message = "通道名称不能为空")
    private String channelName;
    /**
    * 一级通道类型
    */
    @NotNull(message = "一级通道类型不能为空")
    private SmartMessageChannelType1Enum channelType1;
    /**
    * 二级通道类型
    */
    private SmartMessageChannelType2Enum channelType2;
    /**
    * 通道参数
    */
    @NotNull(message = "通道参数不能为空")
    private String channelProperties;
    /**
    * seq
    */
    private Integer seq;
    /**
    * remark
    */
    private String remark;

}