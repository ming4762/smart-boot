package com.smart.kettle.core;

import com.smart.commons.core.data.Tree;
import com.smart.kettle.core.model.RepositoryDirectoryData;
import com.smart.kettle.core.model.RepositoryElementMetaData;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.lang.NonNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * kettle执行器
 * @author ShiZhongMing
 * 2021/7/15 8:41
 * @since 1.0
 */
public class KettleActuator {

    private KettleActuator() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 不显示控制台日志参数
     */
    private static final String KETTLE_DISABLE_CONSOLE_LOGGING = "KETTLE_DISABLE_CONSOLE_LOGGING";

    /**
     * 关闭控制台日志
     */
    public static void closeConsoleLogging() {
        System.setProperty(KETTLE_DISABLE_CONSOLE_LOGGING, "Y");
    }

    /**
     * 打开控制台日志
     */
    public static void openConsoleLogging() {
        System.clearProperty(KETTLE_DISABLE_CONSOLE_LOGGING);
    }

    /**
     * 获取TransMeta
     * @param ktrPath 转换路径
     */
    @SneakyThrows
    public static TransMeta getTransMeta(@NonNull String ktrPath) {
        initKettle();
        return new TransMeta(ktrPath);
    }

    /**
     * 获取TransMeta
     * @param inputStream 转换流
     */
    @SneakyThrows
    public static TransMeta getTransMeta(@NonNull InputStream inputStream) {
        initKettle();
        return new TransMeta(inputStream, null, true, null, null);
    }

    /**
     * 执行数据库trans
     * @param repository 资源库
     * @param transName 转换名
     * @param directoryName 目录名
     */
    @SneakyThrows
    public static TransMeta getDbTransMeta(@NonNull KettleDatabaseRepository repository, @NonNull String transName, String directoryName) {
        initKettle();
        RepositoryDirectoryInterface directoryInterface = getDirectoryInterface(repository, directoryName);
        return repository.loadTransformation(transName, directoryInterface, null, true, null);
    }

    /**
     * 执行转换
     * @param transMeta 转换参数
     * @param params 参数
     * @param variableMap 变量
     * @param parameter 命名参数
     * @param logLevel 日志级别
     * @param beforeHandler 执行前回调
     */
    @SneakyThrows
    public static Trans executeTransfer(
            @NonNull TransMeta transMeta,
            @NonNull String[] params,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameter,
            @NonNull LogLevel logLevel,
            Consumer<Trans> beforeHandler
            ) {
        Trans trans = new Trans(transMeta);
        // 设置变量
        variableMap.forEach(trans::setVariable);
        // 设置命名参数
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            trans.setParameterValue(entry.getKey(), entry.getValue());
        }

        trans.setLogLevel(logLevel);
        if (beforeHandler != null) {
            beforeHandler.accept(trans);
        }
        // 执行转换
        trans.execute(params);
        // 等待转换完成
        trans.waitUntilFinished();
        return trans;
    }

    /**
     * 获取资源库JOB MATE
     * @param repository kettle资源库
     * @param jobName job名称
     * @param directoryName 目录
     */
    @SneakyThrows
    public static JobMeta getDbJobMate(@NonNull KettleDatabaseRepository repository, @NonNull String jobName, String directoryName) {
        initKettle();
        RepositoryDirectoryInterface directoryInterface = getDirectoryInterface(repository, directoryName);
        return repository.loadJob(jobName, directoryInterface, null, null);
    }

    /**
     * 获取JobMate
     * @param path job文件路径
     */
    @SneakyThrows
    public static JobMeta getJobMeta(@NonNull String path) {
        initKettle();
        return new JobMeta(path, null);
    }

    /**
     * 通过InputStream获取JobMate
     * @param inputStream 输入流
     */
    @SneakyThrows
    public static JobMeta getJobMeta(@NonNull InputStream inputStream) {
        initKettle();
        return new JobMeta(inputStream, null, null);
    }


    /**
     * 执行job
     * @param repository 资源库
     * @param jobMeta job元数据
     * @param params job参数
     * @param parameterMap 命名参数
     * @param beforeHandler 执行前回调
     */
    @SneakyThrows
    public static Job executeJob(
            KettleDatabaseRepository repository,
            @NonNull JobMeta jobMeta,
            @NonNull Map<String, String> params,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Job> beforeHandler
    ) {
        Job job = new Job(repository, jobMeta);
        params.forEach(job :: setVariable);
        // 设置命名参数
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            job.setParameterValue(entry.getKey(), entry.getValue());
        }

        job.setLogLevel(logLevel);
        if (beforeHandler != null) {
            beforeHandler.accept(job);
        }
        job.start();
        job.waitUntilFinished();
        return job;
    }

    /**
     * 初始化kettle环境
     */
    @SneakyThrows
    public static void initKettle() {
        if (!KettleEnvironment.isInitialized()) {
            KettleEnvironment.init();
            EnvUtil.environmentInit();
        }
    }

    /**
     * 获取资源路径
     * @param repository 资源库
     * @param directoryName 目录名
     * @return 目录路径
     */
    @SneakyThrows
    private static RepositoryDirectoryInterface getDirectoryInterface(@NonNull KettleDatabaseRepository repository, String directoryName) {
        RepositoryDirectoryInterface directoryInterface = repository.loadRepositoryDirectoryTree();
        if (StringUtils.isNotBlank(directoryName)) {
            directoryInterface = directoryInterface.findDirectory(directoryName);
        }
        return directoryInterface;
    }

    @SneakyThrows
    public static Tree<RepositoryDirectoryData> loadRepositoryData(@NonNull Repository repository, boolean hasDeleted) {
        RepositoryDirectoryInterface repositoryDirectoryInterface = repository.loadRepositoryDirectoryTree();
        return loadRepositoryData(repository, repositoryDirectoryInterface, hasDeleted);
    }

    @SneakyThrows
    private static Tree<RepositoryDirectoryData> loadRepositoryData(@NonNull Repository repository, @NonNull RepositoryDirectoryInterface repositoryDirectoryInterface, boolean hasDeleted) {
        // 获取job trans数据
        List<RepositoryElementMetaInterface> repositoryElementMetaInterfaceList = repository.getJobAndTransformationObjects(repositoryDirectoryInterface.getObjectId(), hasDeleted);
        RepositoryDirectoryData repositoryDirectoryData = RepositoryDirectoryData.builder()
                .id(repositoryDirectoryInterface.getObjectId())
                .name(repositoryDirectoryInterface.getName())
                .path(repositoryDirectoryInterface.getPath())
                .jobTransList(repositoryElementMetaInterfaceList == null ? new ArrayList<>(0) :
                                repositoryElementMetaInterfaceList.stream().map(RepositoryElementMetaData::createByRepositoryElementMeta).collect(Collectors.toList())
                        )
                .build();
        if (repositoryDirectoryInterface.getParent() != null) {
            repositoryDirectoryData.setParentId(repositoryDirectoryInterface.getParent().getObjectId());
        }
        // 构建树数据
        Tree<RepositoryDirectoryData> tree = new Tree<>();
        tree.setText(repositoryDirectoryData.getName());
        tree.setData(repositoryDirectoryData);
        tree.setId(repositoryDirectoryData.getId().getId());
        tree.setParentId(repositoryDirectoryData.getParentId() == null ? "0" : repositoryDirectoryData.getParentId().getId());
        // 使用递归构建树
        if (CollectionUtils.isNotEmpty(repositoryDirectoryInterface.getChildren())) {
            tree.setChildren(
                    repositoryDirectoryInterface.getChildren().stream().map(item -> loadRepositoryData(repository, item, hasDeleted)).collect(Collectors.toList())
            );
            tree.setHasChildren(true);
        } else {
            tree.setHasChildren(false);
            tree.setChildren(new ArrayList<>(0));
        }
        return tree;
    }
}
