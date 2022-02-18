package com.smart.monitor.server.core.constants;

import com.smart.monitor.server.core.event.EventCode;

/**
 * @author shizhongming
 * 2021/3/27 7:13 下午
 */
public enum BuiltInEventCodeEnum implements EventCode {
    /**
     * 上线事件
     */
    UP,
    /**
     * 下线事件
     */
    DOWN,
    REGISTER,
    REGISTERED_UPDATE,
    DEREGISTER,
    ONLINE,
    OFFLINE,
    /**
     * 健康状态检测
     */
    HEALTH_CHECK_ERROR,
    HEALTH_CHECK_DOWN,
    HEALTH_CHECK_UP
    ;

    @Override
    public String getCode() {
        return this.name();
    }
}
