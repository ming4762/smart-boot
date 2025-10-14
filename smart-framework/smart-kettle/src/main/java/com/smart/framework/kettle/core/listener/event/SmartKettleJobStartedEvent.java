package com.smart.framework.kettle.core.listener.event;

import org.pentaho.di.job.Job;

/**
 * kettle作业开始事件
 * @author shizhongming
 * 2025/10/13 19:55
 * @since 5.0.0
 */
public class SmartKettleJobStartedEvent extends SmartKettleJobBasicEvent {

    public SmartKettleJobStartedEvent(Job source) {
        super(source);
    }
}
