package com.smart.framework.kettle.core.listener.event;

import org.pentaho.di.job.Job;
import org.springframework.context.ApplicationEvent;

/**
 * kettle作业基础事件
 * @author shizhongming
 * 2025/10/13 19:55
 * @since 5.0.0
 */
public class SmartKettleJobBasicEvent extends ApplicationEvent {

    public SmartKettleJobBasicEvent(Job source) {
        super(source);
    }

    /**
     * 获取kettle作业
     * @return kettle作业
     */
    public Job getJob() {
        return (Job) getSource();
    }
}
