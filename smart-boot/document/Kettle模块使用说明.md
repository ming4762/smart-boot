# Kettle模块使用说明

## 一、配置文件说明

```yaml
smart:
  kettle:
    log:
#      日志数据库连接信息
      type: mysql
      access: jdbc
      name:
      host:
      db:
      port: 3306
      dbUser: root
      dbPassword:
#      是否启用日志
      enable: true
#      转换日志表
      transLogTableName: k_trans_log
#      转换步骤日志表
      stepLogTableName:
#      metrics日志表
      metricsLogTableName:
#      性能日志表
      performanceLogTableName:
#      日志通道日志表
      channelLogTableName:
#      job日志表
      jobLogTableName:
#      job作业项日志表
      jobEntryLogTableName:
```

## 二、模块介绍

### 1、smart-kettle-core

该模块为kettle核心模块，提供kettle 资源库连接池管理、kettle执行服务、日志管理功能、事件系统

核心编程接口如下：

#### 1）kettle数据库资源库提供器

> com.smart.kettle.core.repository.pool.KettleDatabaseRepositoryProvider
>
> 使用spring注入，需要注意：<span style="color:red">KettleDatabaseRepository使用结束后需要调用returnRepository函数归还资源库对象</span>

```java
/**
  * 获取连接池对象
  * @param properties 连接参数
  * @return KettleDatabaseRepository
*/
KettleDatabaseRepository getRepository(KettleDatabaseRepositoryProperties properties)
    
/**
  * 归还连接池对象
  * 资源库使用完毕必须归还
  * @param properties 参数
  * @param repository KettleDatabaseRepository
*/
public void returnRepository(KettleDatabaseRepositoryProperties properties, KettleDatabaseRepository repository)
```

使用示例：

```java
// 获取资源库
KettleDatabaseRepository repository = this.repositoryProvider.getRepository(properties);
// 获取元数据
TransMeta transMeta = KettleActuator.getDbTransMeta(repository, transName, directoryName);
// 初始化日志
this.kettleLogController.initTransLog(transMeta);
// 执行
Trans trans = KettleActuator.executeTransfer(transMeta, params, variableMap, parameterMap, logLevel, beforeHandler);
this.repositoryProvider.returnRepository(properties, repository);
```

#### 2）kettle日志控制器

> com.smart.kettle.core.log.KettleLogController
>
> 提供9个静态函数用于kettle记录日志的控制
>
> kettle执行记录日志可以在配置文件中统一配置，也可通过这些函数进行独立配置
>
> <span style="color:red">配置文件配置全局有效，函数配置线程内有效，函数配置优先级高</span>

```java
/**
     * 启停trans日志
     */
    public static void enableTransLog(boolean enabled, String tableName)

    /**
     * 启停trans步骤日志
     */
    public static void enableStepLog(boolean enabled, String tableName)

    /**
     * 启停trans Metrics日志
     */
    public static void enableMetricsLog(boolean enabled, String tableName)

    /**
     * 启停trans 性能日志
     */
    public static void enablePerformanceLog(boolean enabled, String tableName)

    /**
     * 启停trans 通道日志
     */
    public static void enableTransChannelLog(boolean enabled, String tableName)

    /**
     * 启停 job日志
     */
    public static void enableJobLog(boolean enabled, String tableName)

    /**
     * 启停 job 工作项日志
     */
    public static void enableJobEntryLog(boolean enabled, String tableName)

    /**
     * 启停 job通道日志
     */
    public static void enableJobChannelLog(boolean enabled, String tableName)
    /**
     * 启用日志
     * @param logType 日志类型
     * @param enable 启动/关闭
     * @param tableName 日志表名称（可以指定日志表，也可以使用全局配置的日志表）
     */
    public static void enableLog(@NonNull LogType logType, boolean enable, String tableName)
```

#### 3）kettle执行日志修改接口

上面介绍的日志控制器控制kettle执行记录日志是否保存、保存表名称等，该章节说明如何修改保存的日志字段

kettle保存的默认字段参考附录1表结构

通过实现接口com.smart.kettle.core.log.modifier.LogRecordModifier并注入到spring容器可以实现：

- 删减/添加保存到数据库的字段
- 修改字段内容

可以同时存在多个修改器，通过覆盖getOrder函数控制修改器的执行顺序

gc-kettle-xxl模块提供2个默认的修改器，向 trans执行日志和job执行日志添加2个字段，XXL_EXECUTOR_NAME（xxl执行器名字）、XXL_EXECUTOR_IP（xxl执行器IP）
com.gc.kettle.xxl.modifier.XxlKettleJobLogModifier
com.gc.kettle.xxl.modifier.XxlKettleTransLogModifier


接口说明：

```java
	public interface LogRecordModifier extends Ordered {

    /**
     * 修改日志
     * @param rowMetaAndData 日志数据
     * @param status LogStatus
     * @param subject Trans
     * @param parent Object
     * @return RowMetaAndData
     * @throws Exception Exception
     */
    default RowMetaAndData modifyLogRecord(RowMetaAndData rowMetaAndData, LogStatus status, Object subject, Object parent) throws Exception {
        return rowMetaAndData;
    }

    /**
     * 支持的日志类型
     * @return 支持的日志类型
     */
    Class<? extends BaseLogTable> support();

    /**
     * 修改器序号
     * 越小则执行优先级越高
     * @return int
     */
    @Override
    default int getOrder() {
        return 0;
    }
```

提供8个抽象辅助实现类，分别对应8种执行记录日志

com.smart.kettle.core.log.modifier.AbstractChannelLogRecordModifier
com.smart.kettle.core.log.modifier.AbstractJobEntryLogRecordModifier
com.smart.kettle.core.log.modifier.AbstractJobLogRecordModifier
com.smart.kettle.core.log.modifier.AbstractLogModifierHandlerSetter
com.smart.kettle.core.log.modifier.AbstractMetricsLogRecordModifier
com.smart.kettle.core.log.modifier.AbstractPerformanceLogRecordModifier
com.smart.kettle.core.log.modifier.AbstractStepLogRecordModifier
com.smart.kettle.core.log.modifier.AbstractTransLogRecordModifier

示例：

> 在trans记录日志中添加字段：XXL_EXECUTOR_NAME、XXL_EXECUTOR_IP

```java
public class XxlKettleTransLogModifier extends AbstractTransLogRecordModifier {

    private XxlProperties xxlProperties;

    @Override
    public RowMetaAndData modifyLogRecord(RowMetaAndData rowMetaAndData, LogStatus status, Object subject, Object parent) throws Exception {
        rowMetaAndData.addValue("XXL_EXECUTOR_NAME", 2, this.xxlProperties.getExecutor().getAppname());
        rowMetaAndData.addValue("XXL_EXECUTOR_IP", 2, IpUtil.getIp());
        return super.modifyLogRecord(rowMetaAndData, status, subject, parent);
    }

    @Autowired
    public void setXxlProperties(XxlProperties xxlProperties) {
        this.xxlProperties = xxlProperties;
    }
}
```



#### 4）kettle执行接口

> com.smart.kettle.core.service.KettleService
>
> 提供2个接口用于执行kettle数据库资源库trans、job，2个接口用于获取kettle资源库树结构

```java
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
    Trans executeDbTransfer(
            @NonNull KettleDatabaseRepositoryProperties properties,
            @NonNull String transName,
            String directoryName,
            @NonNull String[] params,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Trans> beforeHandler
    );

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
    Job executeDbJob(
            @NonNull KettleDatabaseRepositoryProperties properties,
            @NonNull String jobName,
            String directoryName,
            @NonNull Map<String, String> variableMap,
            @NonNull Map<String, String> parameterMap,
            @NonNull LogLevel logLevel,
            Consumer<Job> beforeHandler
    );

	/**
     * 加载资源库树结构
     * @param properties 资源库信息
     * @param hasDeleted 是否包含删除的资源
     * @return 资源库数据
     */
    Tree<RepositoryDirectoryData> loadRepositoryDataTree(@NonNull KettleDatabaseRepositoryProperties properties, boolean hasDeleted);

    /**
     * 加载资源库树结构（包含删除的资源）
     * @param properties 资源库信息
     * @return 资源库数据
     */
    default Tree<RepositoryDirectoryData> loadRepositoryDataTree(@NonNull KettleDatabaseRepositoryProperties properties) {
        return this.loadRepositoryDataTree(properties, true);
    }
```

使用示例：

```java
// 开启转换日志，开启转换步骤日志
KettleLogController.enableTransLog(true, "k_trans_log");
KettleLogController.enableStepLog(true, "k_step_log");
// 执行trans
this.kettleService.executeDbTransfer(
    repositoryProperties,
    "test_trans1",
    "测试目录",
    new String[]{},
    new HashMap<>(0),
    new HashMap<>(0),
    LogLevel.DEBUG,
    trans -> KettleLogStore.getAppender().addLoggingEventListener(kettleLoggingEvent -> log.info(kettleLoggingEvent.toString()))
);
```

#### 5）kettle事件与spring集成

> kettle事件系统与spring集成，kettle事件接口实现类注入spring

支持以下事件：

- TransListener

  org.pentaho.di.trans.TransListener

  trans事件，包含 started、active、finished事件

- TransStoppedListener

  org.pentaho.di.trans.TransStoppedListener

  转换停止事件

- JobListener

  org.pentaho.di.job.JobListener

  包含job started、finished事件

  示例代码：

  ```java
  @Component
  @Slf4j
  public class DemoListener extends AbstractTransListener {
  
      @Override
      public void transFinished(Trans trans) throws KettleException {
          trans.getBatchId();
          log.info("============== kettle执行结束 ===================");
          log.info(trans.getName());
      }
  }
  ```

### 2、smart-kettle-xxl

> XXL kettle对接模块
>
> 基于 XXL-JOB 2.3.0版本对接，如需其他版本请参考代码调整
>
> 提供2个XXL任务接口 用于执行kettle资源库trans和job
>
> 接口接受参数为 类：com.smart.kettle.xxl.pojo.dto.KettleJobExecuteDTO 和 类 com.smart.kettle.xxl.pojo.dto.KettleTransExecuteDTO的json字符串

代码介绍：

```java
@Slf4j
public class XxlKettleExecuteHandler {

    private final KettleService kettleService;

    public XxlKettleExecuteHandler(KettleService kettleService) {
        this.kettleService = kettleService;
    }

    /**
     * 执行数据库转换
     */
    @XxlJob("executeKettleDbTrans")
    public void executeKettleDbTrans() {
        try {
            KettleTransExecuteDTO parameter = JsonUtils.parse(XxlJobHelper.getJobParam(), KettleTransExecuteDTO.class);
            this.kettleService.executeDbTransfer(
                    parameter.getKettleDatabaseRepositoryProperties(),
                    parameter.getTransName(),
                    parameter.getDirectoryName(),
                    parameter.getParams().toArray(new String[0]),
                    parameter.getVariableMap(),
                    parameter.getParameterMap(),
                    parameter.getLogLevel(),
                    trans -> KettleLogStore.getAppender().addLoggingEventListener(kettleLoggingEvent -> XxlJobHelper.log(kettleLoggingEvent.getMessage().toString()))
            );
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    /**
     * 执行数据库job
     */
    @XxlJob("executeKettleDbJob")
    public void executeKettleDbJob() {
        try {
            KettleJobExecuteDTO parameter = JsonUtils.parse(XxlJobHelper.getJobParam(), KettleJobExecuteDTO.class);
            this.kettleService.executeDbJob(
                    parameter.getKettleDatabaseRepositoryProperties(),
                    parameter.getJobName(),
                    parameter.getDirectoryName(),
                    parameter.getVariableMap(),
                    parameter.getParameterMap(),
                    parameter.getLogLevel(),
                    job -> KettleLogStore.getAppender().addLoggingEventListener(kettleLoggingEvent -> XxlJobHelper.log(kettleLoggingEvent.toString()))
            );
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}
```



## 三、使用说明

### 1、初始化日志数据库和kettle资源库数据库

#### 1）kettle资源库数据库

参考kettle文档进行初始化

#### 2）初始化kettle执行日志数据库

初始化脚本详见附表1

### 3、添加trans任务executeKettleDbTrans

执行器函数：com.smart.starter.kettle.xxl.handler.XxlKettleExecuteHandler

执行参数示例：

> 参数为类 com.smart.starter.kettle.xxl.pojo.dto.KettleTransExecuteDTO的json字符串

```json
{"kettleDatabaseRepositoryProperties":{"type":"MYSQL","access":"jdbc","name":null,"host":"192.168.199.14","db":"kettle_repository","port":"3306","dbUser":"root","dbPassword":"jjy_kettle@123","resUser":"admin","resPassword":"admin","id":"test","repositoryName":"test","description":null},"directoryName":"test","variableMap":{},"parameterMap":{},"logLevel":"DEBUG","transName":"test_trans","params":[]}
```

### 4、添加job任务executeKettleDbJob

执行器函数：com.smart.starter.kettle.xxl.handler.XxlKettleExecuteHandler#executeKettleDbJob

参数示例：

> 参数为类com.smart.starter.kettle.xxl.pojo.dto.KettleJobExecuteDTO的json字符串

```json
{"kettleDatabaseRepositoryProperties":{"type":"MYSQL","access":"jdbc","name":null,"host":"192.168.199.14","db":"kettle_repository","port":"3306","dbUser":"root","dbPassword":"jjy_kettle@123","resUser":"admin","resPassword":"admin","id":"test","repositoryName":"test","description":null},"directoryName":"test","variableMap":{},"parameterMap":{},"logLevel":"DEBUG","jobName":"test_job"}
```

## 附表1 
### 1、mysql初始化脚本
```sql
-- 转换日志表
create table k_trans_log
(
    ID_BATCH          int          null,
    CHANNEL_ID        varchar(255) null,
    TRANSNAME         varchar(255) null,
    STATUS            varchar(15)  null,
    LINES_READ        bigint       null,
    LINES_WRITTEN     bigint       null,
    LINES_UPDATED     bigint       null,
    LINES_INPUT       bigint       null,
    LINES_OUTPUT      bigint       null,
    LINES_REJECTED    bigint       null,
    ERRORS            bigint       null,
    STARTDATE         datetime     null,
    ENDDATE           datetime     null,
    LOGDATE           datetime     null,
    DEPDATE           datetime     null,
    REPLAYDATE        datetime     null,
    LOG_FIELD         longtext     null,
    EXECUTING_SERVER  varchar(255) null,
    EXECUTING_USER    varchar(255) null,
    CLIENT            varchar(255) null,
    XXL_EXECUTOR_NAME varchar(255) null,
    XXL_EXECUTOR_IP   varchar(255) null
);

create index IDX_k_trans_log_1
    on k_trans_log (ID_BATCH);

create index IDX_k_trans_log_2
    on k_trans_log (ERRORS, STATUS, TRANSNAME);

-- 步骤日志表
CREATE TABLE k_step_log
(
    ID_BATCH       INT,
    CHANNEL_ID     VARCHAR(255),
    LOG_DATE       DATETIME,
    TRANSNAME      VARCHAR(255),
    STEPNAME       VARCHAR(255),
    STEP_COPY      INT,
    LINES_READ     BIGINT,
    LINES_WRITTEN  BIGINT,
    LINES_UPDATED  BIGINT,
    LINES_INPUT    BIGINT,
    LINES_OUTPUT   BIGINT,
    LINES_REJECTED BIGINT,
    ERRORS         BIGINT,
    LOG_FIELD      LONGTEXT
);

-- 运行日志表
CREATE TABLE k_performance_log
(
    ID_BATCH           INT,
    SEQ_NR             INT,
    LOGDATE            DATETIME,
    TRANSNAME          VARCHAR(255),
    STEPNAME           VARCHAR(255),
    STEP_COPY          INT,
    LINES_READ         BIGINT,
    LINES_WRITTEN      BIGINT,
    LINES_UPDATED      BIGINT,
    LINES_INPUT        BIGINT,
    LINES_OUTPUT       BIGINT,
    LINES_REJECTED     BIGINT,
    ERRORS             BIGINT,
    INPUT_BUFFER_ROWS  BIGINT,
    OUTPUT_BUFFER_ROWS BIGINT
);

-- 日志通道日志表
CREATE TABLE k_channel_log
(
    ID_BATCH             INT,
    CHANNEL_ID           VARCHAR(255),
    LOG_DATE             DATETIME,
    LOGGING_OBJECT_TYPE  VARCHAR(255),
    OBJECT_NAME          VARCHAR(255),
    OBJECT_COPY          VARCHAR(255),
    REPOSITORY_DIRECTORY VARCHAR(255),
    FILENAME             VARCHAR(255),
    OBJECT_ID            VARCHAR(255),
    OBJECT_REVISION      VARCHAR(255),
    PARENT_CHANNEL_ID    VARCHAR(255),
    ROOT_CHANNEL_ID      VARCHAR(255)
);

-- Metrics log table
CREATE TABLE k_metrics_log
(
    ID_BATCH            INT,
    CHANNEL_ID          VARCHAR(255),
    LOG_DATE            DATETIME,
    METRICS_DATE        DATETIME,
    METRICS_CODE        VARCHAR(255),
    METRICS_DESCRIPTION VARCHAR(255),
    METRICS_SUBJECT     VARCHAR(255),
    METRICS_TYPE        VARCHAR(255),
    METRICS_VALUE       BIGINT
);

-- 作业日志表
create table k_job_log
(
    ID_JOB            int          null,
    CHANNEL_ID        varchar(255) null,
    JOBNAME           varchar(255) null,
    STATUS            varchar(15)  null,
    LINES_READ        bigint       null,
    LINES_WRITTEN     bigint       null,
    LINES_UPDATED     bigint       null,
    LINES_INPUT       bigint       null,
    LINES_OUTPUT      bigint       null,
    LINES_REJECTED    bigint       null,
    ERRORS            bigint       null,
    STARTDATE         datetime     null,
    ENDDATE           datetime     null,
    LOGDATE           datetime     null,
    DEPDATE           datetime     null,
    REPLAYDATE        datetime     null,
    LOG_FIELD         longtext     null,
    EXECUTING_SERVER  varchar(255) null,
    EXECUTING_USER    varchar(255) null,
    START_JOB_ENTRY   varchar(255) null,
    CLIENT            varchar(255) null,
    XXL_EXECUTOR_NAME varchar(255) null,
    XXL_EXECUTOR_IP   varchar(255) null
);

create index IDX_k_job_log_1
    on k_job_log (ID_JOB);

create index IDX_k_job_log_2
    on k_job_log (ERRORS, STATUS, JOBNAME);

-- 作业项日志表
CREATE TABLE k_job_item_log
(
  ID_BATCH INT
, CHANNEL_ID VARCHAR(255)
, LOG_DATE DATETIME
, TRANSNAME VARCHAR(255)
, STEPNAME VARCHAR(255)
, LINES_READ BIGINT
, LINES_WRITTEN BIGINT
, LINES_UPDATED BIGINT
, LINES_INPUT BIGINT
, LINES_OUTPUT BIGINT
, LINES_REJECTED BIGINT
, ERRORS BIGINT
, RESULT VARCHAR(5)
, NR_RESULT_ROWS BIGINT
, NR_RESULT_FILES BIGINT
, LOG_FIELD LONGTEXT
, COPY_NR INT
);
CREATE INDEX IDX_k_job_item_log_1 ON k_job_item_log(ID_BATCH);

-- JOB日志通道日志表
CREATE TABLE k_job_channel_log
(
  ID_BATCH INT
, CHANNEL_ID VARCHAR(255)
, LOG_DATE DATETIME
, LOGGING_OBJECT_TYPE VARCHAR(255)
, OBJECT_NAME VARCHAR(255)
, OBJECT_COPY VARCHAR(255)
, REPOSITORY_DIRECTORY VARCHAR(255)
, FILENAME VARCHAR(255)
, OBJECT_ID VARCHAR(255)
, OBJECT_REVISION VARCHAR(255)
, PARENT_CHANNEL_ID VARCHAR(255)
, ROOT_CHANNEL_ID VARCHAR(255)
);

```

### 2、Oracle初始化脚本

```sql
-- 转换日志表
CREATE TABLE K_TRANS_LOG
(
  ID_BATCH INTEGER
, CHANNEL_ID VARCHAR2(255)
, TRANSNAME VARCHAR2(255)
, STATUS VARCHAR2(15)
, LINES_READ INTEGER
, LINES_WRITTEN INTEGER
, LINES_UPDATED INTEGER
, LINES_INPUT INTEGER
, LINES_OUTPUT INTEGER
, LINES_REJECTED INTEGER
, ERRORS INTEGER
, STARTDATE DATE
, ENDDATE DATE
, LOGDATE DATE
, DEPDATE DATE
, REPLAYDATE DATE
, LOG_FIELD CLOB
, EXECUTING_SERVER VARCHAR2(255)
, EXECUTING_USER VARCHAR2(255)
, CLIENT VARCHAR2(255)
)
;
CREATE INDEX IDX_K_TRANS_LOG_1 ON K_TRANS_LOG(ID_BATCH)
;
CREATE INDEX IDX_K_TRANS_LOG_2 ON K_TRANS_LOG(ERRORS, STATUS, TRANSNAME)
;
CREATE INDEX IDX_K_TRANS_LOG_3 ON K_TRANS_LOG(TRANSNAME, LOGDATE)
;

-- 步骤日志表
CREATE TABLE K_STEP_LOG
(
  ID_BATCH INTEGER
, CHANNEL_ID VARCHAR2(255)
, LOG_DATE DATE
, TRANSNAME VARCHAR2(255)
, STEPNAME VARCHAR2(255)
, STEP_COPY INTEGER
, LINES_READ INTEGER
, LINES_WRITTEN INTEGER
, LINES_UPDATED INTEGER
, LINES_INPUT INTEGER
, LINES_OUTPUT INTEGER
, LINES_REJECTED INTEGER
, ERRORS INTEGER
, LOG_FIELD CLOB
)
;
CREATE INDEX IDX_K_STEP_LOG_1 ON K_STEP_LOG(TRANSNAME, LOG_DATE)
;

-- 运行日志表
CREATE TABLE K_PERFORMANCE_LOG
(
  ID_BATCH INTEGER
, SEQ_NR INTEGER
, LOGDATE DATE
, TRANSNAME VARCHAR2(255)
, STEPNAME VARCHAR2(255)
, STEP_COPY INTEGER
, LINES_READ INTEGER
, LINES_WRITTEN INTEGER
, LINES_UPDATED INTEGER
, LINES_INPUT INTEGER
, LINES_OUTPUT INTEGER
, LINES_REJECTED INTEGER
, ERRORS INTEGER
, INPUT_BUFFER_ROWS INTEGER
, OUTPUT_BUFFER_ROWS INTEGER
)
;
CREATE INDEX IDX_K_PERFORMANCE_LOG_1 ON K_PERFORMANCE_LOG(TRANSNAME, LOGDATE)
;

-- 日志通道日志表
CREATE TABLE K_CHANNEL_LOG
(
  ID_BATCH INTEGER
, CHANNEL_ID VARCHAR2(255)
, LOG_DATE DATE
, LOGGING_OBJECT_TYPE VARCHAR2(255)
, OBJECT_NAME VARCHAR2(255)
, OBJECT_COPY VARCHAR2(255)
, REPOSITORY_DIRECTORY VARCHAR2(255)
, FILENAME VARCHAR2(255)
, OBJECT_ID VARCHAR2(255)
, OBJECT_REVISION VARCHAR2(255)
, PARENT_CHANNEL_ID VARCHAR2(255)
, ROOT_CHANNEL_ID VARCHAR2(255)
)
;


-- Metrics log table
CREATE TABLE K_METRICS_LOG
(
  ID_BATCH INTEGER
, CHANNEL_ID VARCHAR2(255)
, LOG_DATE DATE
, METRICS_DATE DATE
, METRICS_CODE VARCHAR2(255)
, METRICS_DESCRIPTION VARCHAR2(255)
, METRICS_SUBJECT VARCHAR2(255)
, METRICS_TYPE VARCHAR2(255)
, METRICS_VALUE INTEGER
)
;

-- 作业日志表
--

CREATE TABLE K_JOB_LOG
(
  ID_JOB INTEGER
, CHANNEL_ID VARCHAR2(255)
, JOBNAME VARCHAR2(255)
, STATUS VARCHAR2(15)
, LINES_READ INTEGER
, LINES_WRITTEN INTEGER
, LINES_UPDATED INTEGER
, LINES_INPUT INTEGER
, LINES_OUTPUT INTEGER
, LINES_REJECTED INTEGER
, ERRORS INTEGER
, STARTDATE DATE
, ENDDATE DATE
, LOGDATE DATE
, DEPDATE DATE
, REPLAYDATE DATE
, LOG_FIELD CLOB
, EXECUTING_SERVER VARCHAR2(255)
, EXECUTING_USER VARCHAR2(255)
, START_JOB_ENTRY VARCHAR2(255)
, CLIENT VARCHAR2(255)
)
;
CREATE INDEX IDX_K_JOB_LOG_1 ON K_JOB_LOG(ID_JOB)
;
CREATE INDEX IDX_K_JOB_LOG_2 ON K_JOB_LOG(ERRORS, STATUS, JOBNAME)
;
CREATE INDEX IDX_K_JOB_LOG_3 ON K_JOB_LOG(JOBNAME, LOGDATE)
;


-- 作业项日志表
--

CREATE TABLE K_JOB_ITEM_LOG
(
  ID_BATCH INTEGER
, CHANNEL_ID VARCHAR2(255)
, LOG_DATE DATE
, TRANSNAME VARCHAR2(255)
, STEPNAME VARCHAR2(255)
, LINES_READ INTEGER
, LINES_WRITTEN INTEGER
, LINES_UPDATED INTEGER
, LINES_INPUT INTEGER
, LINES_OUTPUT INTEGER
, LINES_REJECTED INTEGER
, ERRORS INTEGER
, RESULT VARCHAR2(5)
, NR_RESULT_ROWS INTEGER
, NR_RESULT_FILES INTEGER
, LOG_FIELD CLOB
, COPY_NR INTEGER
)
;
CREATE INDEX IDX_K_JOB_ITEM_LOG_1 ON K_JOB_ITEM_LOG(ID_BATCH)
;
CREATE INDEX IDX_K_JOB_ITEM_LOG_2 ON K_JOB_ITEM_LOG(TRANSNAME, LOG_DATE)
;
```

