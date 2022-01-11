package com.smart.kettle.core.log;

import com.smart.kettle.core.log.modifier.LogModifierHandler;
import com.smart.kettle.core.log.modifier.LogModifierHandlerSetter;
import com.smart.kettle.core.log.type.JobLogType;
import com.smart.kettle.core.log.type.LogType;
import com.smart.kettle.core.log.type.TransLogType;
import com.smart.kettle.core.properties.LogDatabaseProperties;
import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.logging.BaseLogTable;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.TransMeta;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * 日志控制器
 * @author ShiZhongMing
 * 2020/8/24 9:54
 * @since 1.0
 */
public class KettleLogController {

    private final LogDatabaseProperties logDatabaseProperties;

    private final LogModifierHandler logModifierHandler;

    public KettleLogController(LogDatabaseProperties logDatabaseProperties, LogModifierHandler logModifierHandler) {
        this.logDatabaseProperties = logDatabaseProperties;
        this.logModifierHandler = logModifierHandler;
    }

    /**
     * 启停trans日志
     */
    public static void enableTransLog(boolean enabled, String tableName) {
        enableLog(TransLogType.TRANS_LOG, enabled, tableName);
    }

    /**
     * 启停trans步骤日志
     */
    public static void enableStepLog(boolean enabled, String tableName) {
        enableLog(TransLogType.STEP_LOG, enabled, tableName);
    }

    /**
     * 启停trans Metrics日志
     */
    public static void enableMetricsLog(boolean enabled, String tableName) {
        enableLog(TransLogType.METRICS_LOG, enabled, tableName);
    }

    /**
     * 启停trans 性能日志
     */
    public static void enablePerformanceLog(boolean enabled, String tableName) {
        enableLog(TransLogType.PERFORMANCE_LOG, enabled, tableName);
    }

    /**
     * 启停trans 通道日志
     */
    public static void enableTransChannelLog(boolean enabled, String tableName) {
        enableLog(TransLogType.TRANS_CHANNEL_LOG, enabled, tableName);
    }

    /**
     * 启停 job日志
     */
    public static void enableJobLog(boolean enabled, String tableName) {
        enableLog(JobLogType.JOB_LOG, enabled, tableName);
    }

    /**
     * 启停 job 工作项日志
     */
    public static void enableJobEntryLog(boolean enabled, String tableName) {
        enableLog(JobLogType.JOB_ENTRY_LOG, enabled, tableName);
    }

    /**
     * 启停 job通道日志
     */
    public static void enableJobChannelLog(boolean enabled, String tableName) {
        enableLog(JobLogType.JOB_CHANNEL_LOG, enabled, tableName);
    }

    /**
     * 启用日志
     * @param logType 日志类型
     * @param enable 启动/关闭
     * @param tableName 日志表名称（可以指定日志表，也可以使用全局配置的日志表）
     */
    public static void enableLog(@NonNull LogType logType, boolean enable, String tableName) {
        KettleLogConfig logConfig = KettleLogConfigHolder.getConfig(logType);
        logConfig.setEnable(enable);
        logConfig.setTableName(tableName);
    }


    /**
     * 初始化转换日志
     * @param transMeta 转换元数据
     */
    public void initTransLog(@NonNull TransMeta transMeta) {
        // 创建数据库元数据
        DatabaseMeta databaseMeta = this.createLogDbMeta();
        transMeta.addDatabase(databaseMeta);
        Arrays.asList(TransLogType.values()).forEach(logType -> {
            if (this.isEnableTransLog(logType)) {
                KettleLogConfig logConfig = KettleLogConfigHolder.getConfig(logType);
                BaseLogTable logTable = logType.getGetFunction().apply(transMeta);
                if (logTable instanceof LogModifierHandlerSetter) {
                    ((LogModifierHandlerSetter) logTable).setLogModifierHandler(this.logModifierHandler);
                }
                logTable.setConnectionName(databaseMeta.getName());
                logTable.setTableName(StringUtils.isBlank(logConfig.getTableName()) ? this.getGlobalTransLogTableName(logType) : logConfig.getTableName());
                logType.getSetFunction().accept(transMeta, logTable);
            }
        });
    }

    /**
     * 初始化job日志
     * @param jobMeta job元数据
     */
    public void initJobLog(@NonNull JobMeta jobMeta) {
        // 创建数据库元数据
        DatabaseMeta databaseMeta = this.createLogDbMeta();
        jobMeta.addDatabase(databaseMeta);
        Arrays.asList(JobLogType.values()).forEach(logType -> {
            if (this.isEnableJobLog(logType)) {
                KettleLogConfig logConfig = KettleLogConfigHolder.getConfig(logType);
                BaseLogTable logTable = logType.getGetFunction().apply(jobMeta);
                if (logTable instanceof LogModifierHandlerSetter) {
                    ((LogModifierHandlerSetter) logTable).setLogModifierHandler(this.logModifierHandler);
                }
                logTable.setConnectionName(databaseMeta.getName());
                logTable.setTableName(StringUtils.isBlank(logConfig.getTableName()) ? this.getGlobalJobLogTableName(logType) : logConfig.getTableName());
                logType.getSetFunction().accept(jobMeta, logTable);
            }
        });
    }

    /**
     * 是否启用日志
     */
    private boolean isEnableTransLog(TransLogType logType) {
        KettleLogConfig logConfig = KettleLogConfigHolder.getConfig(logType);
        if (Boolean.TRUE.equals(logConfig.getEnable())) {
            String tableName = StringUtils.isBlank(logConfig.getTableName()) ? this.getGlobalTransLogTableName(logType) : logConfig.getTableName();
            Assert.notNull(tableName, String.format("必须指定日志表，日志表类型：%s", logType.name()));
            return true;
        } else {
            return logConfig.getEnable() == null && this.isEnable() && this.getGlobalTransLogTableName(logType) != null;
        }
    }

    /**
     * 是否启用job日志
     */
    private boolean isEnableJobLog(JobLogType logType) {
        KettleLogConfig logConfig = KettleLogConfigHolder.getConfig(logType);
        if (Boolean.TRUE.equals(logConfig.getEnable())) {
            String tableName = StringUtils.isBlank(logConfig.getTableName()) ? this.getGlobalJobLogTableName(logType) : logConfig.getTableName();
            Assert.notNull(tableName, String.format("必须指定日志表，日志表类型：%s", logType.name()));
            return true;
        } else {
            return logConfig.getEnable() == null && this.isEnable() && this.getGlobalJobLogTableName(logType) != null;
        }
    }

    private boolean isEnable() {
        return Boolean.TRUE.equals(this.logDatabaseProperties.getEnable());
    }

    /**
     * 获取全局的job日志表名称
     */
    private String getGlobalJobLogTableName(JobLogType logType) {
        switch (logType) {
            case JOB_LOG:
                return this.logDatabaseProperties.getJobLogTableName();
            case JOB_ENTRY_LOG:
                return this.logDatabaseProperties.getJobEntryLogTableName();
            case JOB_CHANNEL_LOG:
                return this.logDatabaseProperties.getChannelLogTableName();
            default:
                return null;
        }
    }

    /**
     * 获取全局的日志表名称
     */
    private String getGlobalTransLogTableName(TransLogType logType) {
        String tableName = null;
        switch (logType) {
            case TRANS_LOG:
                tableName = this.logDatabaseProperties.getTransLogTableName();
                break;
            case STEP_LOG:
                tableName = this.logDatabaseProperties.getStepLogTableName();
                break;
            case METRICS_LOG:
                tableName = this.logDatabaseProperties.getMetricsLogTableName();
                break;
            case PERFORMANCE_LOG:
                tableName = this.logDatabaseProperties.getPerformanceLogTableName();
                break;
            case TRANS_CHANNEL_LOG:
                tableName = this.logDatabaseProperties.getChannelLogTableName();
                break;
            default:
                break;
        }
        return tableName;
    }

    /**
     * 创建日志DatabaseMeta
     * @return DatabaseMeta
     */
    private DatabaseMeta createLogDbMeta() {
        Assert.notNull(this.logDatabaseProperties.getDb(), "请配置kettle日志数据库信息");
        DatabaseMeta databaseMeta = new DatabaseMeta();
        databaseMeta.setName(StringUtils.isBlank(this.logDatabaseProperties.getName()) ? LogDatabaseProperties.DEFAULT_DB_NAME : this.logDatabaseProperties.getName());
        databaseMeta.setDatabaseType(this.logDatabaseProperties.getType());
        databaseMeta.setHostname(this.logDatabaseProperties.getHost());
        databaseMeta.setDBPort(this.logDatabaseProperties.getPort());
        databaseMeta.setDBName(this.logDatabaseProperties.getDb());
        databaseMeta.setUsername(this.logDatabaseProperties.getDbUser());
        databaseMeta.setPassword(this.logDatabaseProperties.getDbPassword());
        return databaseMeta;
    }
}
