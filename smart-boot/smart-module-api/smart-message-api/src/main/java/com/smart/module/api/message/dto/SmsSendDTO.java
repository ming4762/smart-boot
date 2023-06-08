package com.smart.module.api.message.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 短信发送返回结果
 * @author zhongming4762
 * 2023/6/6
 */
@Getter
@Setter
@ToString
public class SmsSendDTO implements Serializable {

    private String responseData;

    private Long channelId;

    private String channelCode;

    private String channelType;
}
