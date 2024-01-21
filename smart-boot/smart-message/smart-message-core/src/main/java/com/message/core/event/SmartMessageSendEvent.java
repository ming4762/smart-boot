package com.message.core.event;

import com.smart.module.api.message.constants.MessageChannelEnum;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * @author shizhongming
 * 2023/1/21 19:51
 * @since 3.0.0
 */
@Getter
public class SmartMessageSendEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 231582669104791540L;

    private final RemoteMessageSendParameter sendParameter;

    private final Map<MessageChannelEnum, List<MessageSendDTO>> sendResult;

    public SmartMessageSendEvent(RemoteMessageSendParameter sendParameter, Map<MessageChannelEnum, List<MessageSendDTO>> sendResult, Object source) {
        super(source);
        this.sendParameter = sendParameter;
        this.sendResult = sendResult;
    }
}
