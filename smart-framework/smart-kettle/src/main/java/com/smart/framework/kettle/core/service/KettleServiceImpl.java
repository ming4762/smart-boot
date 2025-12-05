package com.smart.framework.kettle.core.service;

import com.smart.framework.commons.core.data.Tree;
import com.smart.framework.kettle.core.KettleActuator;
import com.smart.framework.kettle.core.KettleProperties;
import com.smart.framework.kettle.core.listener.SmartKettleGlobalAgentListener;
import com.smart.framework.kettle.core.log.KettleLogController;
import com.smart.framework.kettle.core.model.RepositoryDirectoryData;
import com.smart.framework.kettle.core.parameter.BasicExecuteParameter;
import com.smart.framework.kettle.core.parameter.TransExecuteParameter;
import com.smart.framework.kettle.core.properties.KettleDatabaseRepositoryProperties;
import com.smart.framework.kettle.core.repository.pool.KettleDatabaseRepositoryProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author ShiZhongMing
 * 2021/7/15 10:44
 * @since 1.0
 */
@RequiredArgsConstructor
public class KettleServiceImpl implements KettleService, ApplicationContextAware {

    private final KettleDatabaseRepositoryProvider repositoryProvider;
    private final KettleLogController kettleLogController;
    private final KettleProperties kettleProperties;

    private ApplicationContext applicationContext;

    /**
     * 执行资源库转换
     * @param properties 资源库配置参数
     * @param transName 转换名
     * @param directoryName 转换所在目录
     * @param parameter 参数
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    @Override
    public Trans executeDbTransfer(
            KettleDatabaseRepositoryProperties properties,
            @NonNull String transName,
            String directoryName,
            @NonNull TransExecuteParameter parameter,
            Consumer<Trans> beforeHandler
    ) {
        KettleDatabaseRepositoryProperties repositoryProperties = Objects.requireNonNullElse(properties, this.getDefaultDbRepositoryProperties());
        // 关闭控制台日志
        KettleActuator.closeConsoleLogging();
        // 获取资源库
        KettleDatabaseRepository repository = this.repositoryProvider.getRepository(repositoryProperties);
        Trans trans;
        try {
            // 获取元数据
            TransMeta transMeta = KettleActuator.getDbTransMeta(repository, transName, directoryName);
            // 执行
            trans = this.doExecuteTrans(transMeta, parameter, beforeHandler);
        } finally {
            this.repositoryProvider.returnRepository(repositoryProperties, repository);
        }
        return trans;
    }

    @Override
    public Trans executeFileTransfer(
            @NonNull String ktrPath,
            @NonNull TransExecuteParameter parameter,
            Consumer<Trans> beforeHandler
    ) {
        TransMeta transMeta = KettleActuator.getTransMeta(ktrPath);
        return this.doExecuteTrans(transMeta, parameter, beforeHandler);
    }

    @Override
    @SneakyThrows(IOException.class)
    public Trans executeClasspathFileTransfer(
            @NonNull String ktrPath,
            @NonNull TransExecuteParameter parameter,
            Consumer<Trans> beforeHandler
    ) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Assert.notNull(classLoader, "execute trans fail, classLoader is null");
        try (InputStream inputStream = classLoader.getResourceAsStream(ktrPath)) {
            Assert.notNull(inputStream, "execute trans fail,can not find trans file");
            TransMeta transMeta = KettleActuator.getTransMeta(inputStream);
            return this.doExecuteTrans(transMeta, parameter, beforeHandler);
        }
    }


    /**
     * 执行资源库Job
     * @param properties 资源库配置参数
     * @param jobName 名称
     * @param directoryName 转换所在目录
     * @param parameter 参数
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    @Override
    public Job executeDbJob(
            @NonNull KettleDatabaseRepositoryProperties properties,
            @NonNull String jobName,
            String directoryName,
            @NonNull BasicExecuteParameter parameter,
            Consumer<Job> beforeHandler
    ) {
        KettleDatabaseRepositoryProperties repositoryProperties = Objects.requireNonNullElse(properties, this.getDefaultDbRepositoryProperties());
        // 获取资源库
        KettleDatabaseRepository repository = this.repositoryProvider.getRepository(repositoryProperties);
        Job job;
        try {
            // 获取元数据
            JobMeta jobMeta = KettleActuator.getDbJobMate(repository, jobName, directoryName);
            job = this.doExecuteJob(jobMeta, repository, parameter, beforeHandler);
        } finally {
            this.repositoryProvider.returnRepository(repositoryProperties, repository);
        }
        return job;
    }

    @Override
    public Job executeFileJob(
            @NonNull String jobPath,
            @NonNull BasicExecuteParameter parameter,
            Consumer<Job> beforeHandler
    ) {
        JobMeta jobMeta = KettleActuator.getJobMeta(jobPath);
        return this.doExecuteJob(jobMeta, null, parameter, beforeHandler);
    }

    @Override
    @SneakyThrows(IOException.class)
    public Job executeClasspathJob(
            @NonNull String jobPath,
            @NonNull BasicExecuteParameter parameter,
            Consumer<Job> beforeHandler
    ) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Assert.notNull(classLoader, "execute trans fail, classLoader is null");
        try (InputStream inputStream = classLoader.getResourceAsStream(jobPath)) {
            Assert.notNull(inputStream, "execute job fail,can not find job file");
            JobMeta jobMeta = KettleActuator.getJobMeta(inputStream);
            return this.doExecuteJob(jobMeta, null, parameter, beforeHandler);
        }
    }

    protected Job doExecuteJob(
            @NonNull JobMeta jobMeta,
            KettleDatabaseRepository repository,
            @NonNull BasicExecuteParameter parameter,
            Consumer<Job> beforeHandler
    ) {
        // 初始化日志
        this.kettleLogController.initJobLog(jobMeta);
        return KettleActuator.executeJob(repository, jobMeta, parameter, job1 -> {
            if (beforeHandler != null) {
                beforeHandler.accept(job1);
            }
            job1.addJobListener(this.createListener());
        });
    }

    protected Trans doExecuteTrans(
            @NonNull TransMeta transMeta,
            @NonNull TransExecuteParameter parameter,
            Consumer<Trans> beforeHandler
    ) {
        // 初始化日志
        this.kettleLogController.initTransLog(transMeta);
        // 执行
        return KettleActuator.executeTransfer(transMeta, parameter, trans1 -> {
            if (beforeHandler != null) {
                beforeHandler.accept(trans1);
            }
            // 添加转换事件监听器
            SmartKettleGlobalAgentListener listener = this.createListener();
            trans1.addTransListener(listener);
            trans1.addTransStoppedListener(listener);
        });
    }

    private SmartKettleGlobalAgentListener createListener() {
        return new SmartKettleGlobalAgentListener(this.applicationContext);
    }

    @Override
    public Tree<RepositoryDirectoryData> loadRepositoryDataTree(@NonNull KettleDatabaseRepositoryProperties properties, boolean hasDeleted) {
        KettleDatabaseRepositoryProperties repositoryProperties = Objects.requireNonNullElse(properties, this.getDefaultDbRepositoryProperties());
        KettleDatabaseRepository repository = this.repositoryProvider.getRepository(repositoryProperties);
        Tree<RepositoryDirectoryData> data = KettleActuator.loadRepositoryData(repository, hasDeleted);
        this.repositoryProvider.returnRepository(repositoryProperties, repository);
        return data;
    }

    protected KettleDatabaseRepositoryProperties getDefaultDbRepositoryProperties() {
        KettleProperties.DbRepository dbRepository = this.kettleProperties.getDbRepository();
        if (!Boolean.TRUE.equals(dbRepository.getEnabled())) {
            return null;
        }
        KettleDatabaseRepositoryProperties.KettleDatabaseRepositoryPropertiesBuilder<?, ?> builder = KettleDatabaseRepositoryProperties.builder()
                .resUser(dbRepository.getResUser())
                .resPassword(dbRepository.getResPassword())
                .repositoryName(dbRepository.getRepositoryName())
                .description(dbRepository.getDescription())

                .type(dbRepository.getType())
                .access(dbRepository.getAccess())
                .name(dbRepository.getName())
                .db(dbRepository.getDb())
                .host(dbRepository.getHost())
                .port(dbRepository.getPort())
                .dbUser(dbRepository.getDbUser())
                .dbPassword(dbRepository.getDbPassword())
                .forceIdentifiersToLowercase(dbRepository.getForceIdentifiersToLowercase())
                .forceIdentifiersToUppercase(dbRepository.getForceIdentifiersToUppercase());
        return builder.build();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
