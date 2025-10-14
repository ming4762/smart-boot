package com.smart.framework.kettle.core.listener.event;

import org.pentaho.di.trans.Trans;

/**
 * kettle转换开始事件
 * @author shizhongming
 * 2025/10/13 19:55
 * @since 5.0.0
 */
public class SmartKettleTransStoppedEvent extends SmartKettleTransBasicEvent {

    public SmartKettleTransStoppedEvent(Trans source) {
        super(source);
    }
}
