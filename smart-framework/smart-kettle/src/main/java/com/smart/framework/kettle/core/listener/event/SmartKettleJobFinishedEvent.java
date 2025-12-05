package com.smart.framework.kettle.core.listener.event;

import org.pentaho.di.job.Job;

/**
 * kettle作业完成事件
 * @author shizhongming
 * 2025/10/13 19:53
 * @since 5.0.0
 */
public class SmartKettleJobFinishedEvent extends SmartKettleJobBasicEvent {

    public SmartKettleJobFinishedEvent(Job source) {
        super(source);
    }
}
