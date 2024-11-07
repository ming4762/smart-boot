package com.smart.framework.kettle.core.service;

import com.smart.framework.commons.core.data.Tree;
import com.smart.framework.kettle.core.model.RepositoryDirectoryData;
import com.smart.framework.kettle.core.parameter.BasicExecuteParameter;
import com.smart.framework.kettle.core.parameter.TransExecuteParameter;
import com.smart.framework.kettle.core.properties.KettleDatabaseRepositoryProperties;
import org.pentaho.di.job.Job;
import org.pentaho.di.trans.Trans;
import org.springframework.lang.NonNull;

import java.util.function.Consumer;

/**
 * @author ShiZhongMing
 * 2021/7/15 10:36
 * @since 1.0
 */
public interface KettleService {


    /**
     * 执行资源库转换
     * @param properties 资源库配置参数
     * @param transName 转换名
     * @param directoryName 转换所在目录
     * @param parameter 参数
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    Trans executeDbTransfer(
            KettleDatabaseRepositoryProperties properties,
            @NonNull String transName,
            String directoryName,
            @NonNull TransExecuteParameter parameter,
            Consumer<Trans> beforeHandler
    );

    /**
     * 执行文件trans
     * @param ktrPath 文件路径
     * @param parameter 参数
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    Trans executeFileTransfer(
            @NonNull String ktrPath,
            @NonNull TransExecuteParameter parameter,
            Consumer<Trans> beforeHandler
    );

    /**
     * 执行文件trans，文件位于项目内
     * @param ktrPath 文件路径
     * @param parameter 参数
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    Trans executeClasspathFileTransfer(
            @NonNull String ktrPath,
            @NonNull TransExecuteParameter parameter,
            Consumer<Trans> beforeHandler
    );

    /**
     * 执行资源库Job
     * @param properties 资源库配置参数
     * @param jobName 名称
     * @param directoryName 转换所在目录
     * @param parameter 参数
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    Job executeDbJob(
            KettleDatabaseRepositoryProperties properties,
            @NonNull String jobName,
            String directoryName,
            @NonNull BasicExecuteParameter parameter,
            Consumer<Job> beforeHandler
    );

    /**
     * 执行文件job
     * @param jobPath job文件路径
     * @param parameter 参数
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    Job executeFileJob(
            @NonNull String jobPath,
            @NonNull BasicExecuteParameter parameter,
            Consumer<Job> beforeHandler
    );

    /**
     * 执行文件job
     * job文件位于classpath
     * @param jobPath job文件路径
     * @param parameter 变量
     * @param beforeHandler 执行前事件
     * @return 执行的转换
     */
    Job executeClasspathJob(
            @NonNull String jobPath,
            @NonNull BasicExecuteParameter parameter,
            Consumer<Job> beforeHandler
    );


    /**
     * 加载资源库树结果
     * @param properties 资源库信息
     * @param hasDeleted 是否包含删除的资源
     * @return 资源库数据
     */
    Tree<RepositoryDirectoryData> loadRepositoryDataTree(@NonNull KettleDatabaseRepositoryProperties properties, boolean hasDeleted);

    /**
     * 加载资源库树结果
     * @param properties 资源库信息
     * @return 资源库数据
     */
    default Tree<RepositoryDirectoryData> loadRepositoryDataTree(@NonNull KettleDatabaseRepositoryProperties properties) {
        return this.loadRepositoryDataTree(properties, true);
    }
}

