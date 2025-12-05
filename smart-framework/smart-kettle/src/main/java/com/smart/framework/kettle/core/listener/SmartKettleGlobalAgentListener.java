package com.smart.framework.kettle.core.listener;

import com.smart.framework.kettle.core.listener.event.*;
import lombok.RequiredArgsConstructor;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobListener;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransListener;
import org.pentaho.di.trans.TransStoppedListener;
import org.springframework.context.ApplicationContext;

/**
 * 全局kettle监听器
 * @author shizhongming
 * 2025/10/13 19:49
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class SmartKettleGlobalAgentListener implements TransListener, TransStoppedListener, JobListener {

    private final ApplicationContext applicationContext;

    /**
     * 作业完成
     * @param job 作业
     */
    @Override
    public void jobFinished(Job job) {
        applicationContext.publishEvent(new SmartKettleJobFinishedEvent(job));
    }

    @Override
    public void jobStarted(Job job) {
        applicationContext.publishEvent(new SmartKettleJobStartedEvent(job));
    }

    @Override
    public void transStarted(Trans trans) {
        applicationContext.publishEvent(new SmartKettleTransStartedEvent(trans));
    }

    @Override
    public void transActive(Trans trans) {
        applicationContext.publishEvent(new SmartKettleTransActiveEvent(trans));
    }

    @Override
    public void transFinished(Trans trans) {
        applicationContext.publishEvent(new SmartKettleTransFinishedEvent(trans));
    }

    @Override
    public void transStopped(Trans trans) {
        applicationContext.publishEvent(new SmartKettleTransStoppedEvent(trans));
    }
}
