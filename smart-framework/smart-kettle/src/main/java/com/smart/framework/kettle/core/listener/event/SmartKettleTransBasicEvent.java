package com.smart.framework.kettle.core.listener.event;

import org.pentaho.di.trans.Trans;
import org.springframework.context.ApplicationEvent;

/**
 * kettle作业基础事件
 * @author shizhongming
 * 2025/10/13 19:55
 * @since 5.0.0
 */
public class SmartKettleTransBasicEvent extends ApplicationEvent {

    public SmartKettleTransBasicEvent(Trans source) {
        super(source);
    }

    /**
     * 获取kettle作业
     * @return kettle作业
     */
    public Trans getTrans() {
        return (Trans) getSource();
    }
}
