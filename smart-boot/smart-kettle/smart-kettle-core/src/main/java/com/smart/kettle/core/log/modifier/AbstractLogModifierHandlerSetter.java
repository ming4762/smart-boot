package com.smart.kettle.core.log.modifier;

/**
 * @author ShiZhongMing
 * 2021/7/22 21:05
 * @since 1.0
 */
public class AbstractLogModifierHandlerSetter implements LogModifierHandlerSetter {

    protected LogModifierHandler logModifierHandler;

    @Override
    public void setLogModifierHandler(LogModifierHandler logModifierHandler) {
        this.logModifierHandler = logModifierHandler;
    }
}
