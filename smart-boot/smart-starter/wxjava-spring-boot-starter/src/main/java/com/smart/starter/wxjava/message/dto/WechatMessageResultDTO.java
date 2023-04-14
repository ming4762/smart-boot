package com.smart.starter.wxjava.message.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.starter.wxjava.constants.WechatEventEnum;
import com.smart.starter.wxjava.constants.WechatMsgTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/4/7
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WechatMessageResultDTO implements Serializable {

    @JsonProperty("ToUserName")
    private String toUserName;

    @JsonProperty("FromUserName")
    private  String fromUserName;

    @JsonProperty("CreateTime")
    private  String createTime;

    @JsonProperty("MsgType")
    private WechatMsgTypeEnum msgType;

    @JsonProperty("Event")
    private WechatEventEnum event;

    @JsonProperty("EventKey")
    private String eventKey;

    @JsonProperty("Ticket")
    private String ticket;


    @JsonProperty("Content")
    private String content;

    @JsonProperty("MsgId")
    private String msgId;
}
