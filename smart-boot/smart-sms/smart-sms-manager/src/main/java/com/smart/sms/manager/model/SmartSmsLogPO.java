package com.smart.sms.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* smart_sms_log - 短息发送日志
* @author SmartCodeGenerator
* 2023年5月30日
*/
@Getter
@Setter
@TableName("smart_sms_log")
public class SmartSmsLogPO extends BaseModelCreateUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * channel_id - 短信通道
    */
    private Long channelId;

    /**
    * is_success - 是否发送成功
    */
    private Boolean isSuccess;

    /**
    * send_parameter - 发送参数
    */
    private String sendParameter;

    /**
    * send_result - 发送结果
    */
    private String sendResult;

    /**
    * error_message - 错误信息
    */
    private String errorMessage;

}