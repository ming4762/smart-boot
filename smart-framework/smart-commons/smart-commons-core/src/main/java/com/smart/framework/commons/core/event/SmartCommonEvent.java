package com.smart.framework.commons.core.event;

import com.smart.framework.commons.core.constants.SmartEventTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * smart-boot 通用事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-11 20:16
 * @since 5.0.0
 */
@Getter
public abstract class SmartCommonEvent extends ApplicationEvent {

    @Setter
    private SmartEventTypeEnum eventType;

    protected SmartCommonEvent(Object source) {
        super(source);
        this.eventType = SmartEventTypeEnum.LOCAL;
    }

    protected SmartCommonEvent() {
        this(SmartEventTypeEnum.LOCAL);
    }

    /**
     * 是否是本地消息
     * @return boolean
     */
    public boolean isLocal() {
        return SmartEventTypeEnum.LOCAL.equals(this.eventType);
    }
}
