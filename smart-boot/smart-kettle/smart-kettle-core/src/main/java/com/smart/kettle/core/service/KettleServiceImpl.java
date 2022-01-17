package com.smart.kettle.core.service;

import com.smart.commons.core.data.Tree;
import com.smart.kettle.core.KettleActuator;
import com.smart.kettle.core.log.KettleLogController;
import com.smart.kettle.core.model.RepositoryDirectoryData;
import com.smart.kettle.core.properties.KettleDatabaseRepositoryProperties;
import com.smart.kettle.core.repository.pool.KettleDatabaseRepositoryProvider;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobListener;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransListener;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.TransStoppedListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/7/15 10:44
 * @since 1.0
 */
public class KettleServiceImpl implements KettleService, ApplicationContextAware {

    private final KettleDatabaseRepositoryProvider repositoryProvider;

    private final KettleLogController kettleLogController;

    /**
     * trans事件列表
     */
    private List<TransListener> transListenerList = new ArrayList<>(0);

    /**
     * trans top 事件列表
     */
    private List<TransStoppedListener> transStoppedListenerList = new ArrayList<>(0);

    /**
     * job 事件列表
     */
    private List<JobListener> jobListenerList = new ArrayList<>(0);

    public KettleServiceImpl(KettleDatabaseRepositoryProvider repositoryProvider, KettleLogController kettleLogController) {
        this.repositoryProvider = repositoryProvider;
        this.kettleLogController = kettleLogController;
    }

    /**
     * 执行资源库转换
     * @param properties 资源库配置参数
     * @param transName 转换名
     * @param directoryName 转换所在目录
     * @param params 参数
     * @param variableMap 变量
     * @param parameterMap  命名参数
     * @param logLevel 日志级别
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    @Override
    public Trans executeDbTransfer(
            @NonNull KettleDatabaseRepositoryProperties properties,
            @NonNull String transName,
            String directoryName,
            @NonNull String[] params,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Trans> beforeHandler
    ) {
        // 关闭控制台日志
        KettleActuator.closeConsoleLogging();
        // 获取资源库
        KettleDatabaseRepository repository = this.repositoryProvider.getRepository(properties);
        Trans trans;
        try {
            // 获取元数据
            TransMeta transMeta = KettleActuator.getDbTransMeta(repository, transName, directoryName);
            // 执行
            trans = this.doExecuteTrans(transMeta, params, variableMap, parameterMap, logLevel, beforeHandler);
        } finally {
            this.repositoryProvider.returnRepository(properties, repository);
        }
        return trans;
    }

    @Override
    public Trans executeFileTransfer(
            @NonNull String ktrPath,
            @NonNull String[] params,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Trans> beforeHandler
    ) {
        TransMeta transMeta = KettleActuator.getTransMeta(ktrPath);
        return this.doExecuteTrans(transMeta, params, variableMap, parameterMap, logLevel, beforeHandler);
    }

    @Override
    public Trans executeClasspathFileTransfer(
            @NonNull String ktrPath,
            @NonNull String[] params,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Trans> beforeHandler
    ) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(ktrPath);
        Assert.notNull(inputStream, "execute trans fail,can not find trans file");
        TransMeta transMeta = KettleActuator.getTransMeta(inputStream);
        return this.doExecuteTrans(transMeta, params, variableMap, parameterMap, logLevel, beforeHandler);
    }


    /**
     * 执行资源库Job
     * @param properties 资源库配置参数
     * @param jobName 名称
     * @param directoryName 转换所在目录
     * @param variableMap 变量
     * @param parameterMap  命名参数
     * @param logLevel 日志级别
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    @Override
    public Job executeDbJob(
            @NonNull KettleDatabaseRepositoryProperties properties,
            @NonNull String jobName,
            String directoryName,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Job> beforeHandler
    ) {
        // 获取资源库
        KettleDatabaseRepository repository = this.repositoryProvider.getRepository(properties);
        Job job;
        try {
            // 获取元数据
            JobMeta jobMeta = KettleActuator.getDbJobMate(repository, jobName, directoryName);
            job = this.doExecuteJob(jobMeta, repository, variableMap, parameterMap, logLevel, beforeHandler);
        } finally {
            this.repositoryProvider.returnRepository(properties, repository);
        }
        return job;
    }

    @Override
    public Job executeFileJob(
            @NonNull String jobPath,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Job> beforeHandler
    ) {
        JobMeta jobMeta = KettleActuator.getJobMeta(jobPath);
        return this.doExecuteJob(jobMeta, null, variableMap, parameterMap, logLevel, beforeHandler);
    }

    @Override
    public Job executeClasspathJob(
            @NonNull String jobPath,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Job> beforeHandler
    ) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(jobPath);
        Assert.notNull(inputStream, "execute job fail,can not find job file");
        JobMeta jobMeta = KettleActuator.getJobMeta(inputStream);
        return this.doExecuteJob(jobMeta, null, variableMap, parameterMap, logLevel, beforeHandler);
    }

    protected Job doExecuteJob(
            @NonNull JobMeta jobMeta,
            KettleDatabaseRepository repository,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Job> beforeHandler
    ) {
        // 初始化日志
        this.kettleLogController.initJobLog(jobMeta);
        return KettleActuator.executeJob(repository, jobMeta, variableMap, parameterMap, logLevel, job1 -> {
            if (beforeHandler != null) {
                beforeHandler.accept(job1);
            }
            this.jobListenerList.forEach(job1 :: addJobListener);
        });
    }

    protected Trans doExecuteTrans(
            @NonNull TransMeta transMeta,
            @NonNull String[] params,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Trans> beforeHandler
    ) {
        // 初始化日志
        this.kettleLogController.initTransLog(transMeta);
        // 执行
        return KettleActuator.executeTransfer(transMeta, params, variableMap, parameterMap, logLevel, trans1 -> {
            if (beforeHandler != null) {
                beforeHandler.accept(trans1);
            }
            // 添加转换事件
            this.transListenerList.forEach(trans1::addTransListener);
            this.transStoppedListenerList.forEach(trans1 :: addTransStoppedListener);
        });
    }

    @Override
    public Tree<RepositoryDirectoryData> loadRepositoryDataTree(@NonNull KettleDatabaseRepositoryProperties properties, boolean hasDeleted) {
        KettleDatabaseRepository repository = this.repositoryProvider.getRepository(properties);
        Tree<RepositoryDirectoryData> data = KettleActuator.loadRepositoryData(repository, hasDeleted);
        this.repositoryProvider.returnRepository(properties, repository);
        return data;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.transListenerList = Arrays.stream(applicationContext.getBeanNamesForType(TransListener.class))
                .map(item -> applicationContext.getBean(item, TransListener.class))
                .collect(Collectors.toList());
        this.transStoppedListenerList = Arrays.stream(applicationContext.getBeanNamesForType(TransStoppedListener.class))
                .map(item -> applicationContext.getBean(item, TransStoppedListener.class))
                .collect(Collectors.toList());
        this.jobListenerList = Arrays.stream(applicationContext.getBeanNamesForType(JobListener.class))
                .map(item -> applicationContext.getBean(item, JobListener.class))
                .collect(Collectors.toList());
    }
}
