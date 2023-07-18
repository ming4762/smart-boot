package com.smart.message.manager.pojo.vo;

import com.smart.message.manager.model.SmartSmsChannelManagerPO;
import com.smart.message.manager.model.SmartSmsLogPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhongming4762
 * 2023/5/31
 */
@Getter
@Setter
@ToString
public class SmartSmsLogListVO extends SmartSmsLogPO {

    private SmartSmsChannelManagerPO channel;
}
