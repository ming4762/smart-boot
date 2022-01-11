package com.smart.kettle.core.log.modifier;

/**
 * @author ShiZhongMing
 * 2021/7/22 19:17
 * @since 1.0
 */
public interface LogModifierHandlerSetter {
    /**
     * 设置日志修改handler
     * @param logModifierHandler LogModifierHandler
     */
    void setLogModifierHandler(LogModifierHandler logModifierHandler);
}
