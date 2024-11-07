package com.smart.framework.kettle.core.xxl;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.kettle.core.parameter.BasicExecuteParameter;
import com.smart.framework.kettle.core.parameter.TransExecuteParameter;
import com.smart.framework.kettle.core.service.KettleService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.job.Job;
import org.pentaho.di.trans.Trans;

/**
 * XXL Kettle执行器
 * @author shizhongming
 * 2021/7/19 6:09 下午
 */
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
            TransExecuteParameter executeParameter = TransExecuteParameter.builder()
                    .variable(parameter.getVariable())
                    .namedParameter(parameter.getNamedParameter())
                    .params(parameter.getParams())
                    .logLevel(parameter.getLogLevel())
                    .build();
            Trans trans = this.kettleService.executeDbTransfer(
                    parameter.getKettleDatabaseRepositoryProperties(),
                    parameter.getTransName(),
                    parameter.getDirectoryName(),
                    executeParameter,
                    null
            );
            XxlJobHelper.log(KettleLogStore.getAppender().getBuffer().toString());
            KettleLogStore.getAppender().clear();
            if (trans.getErrors() > 0) {
                XxlJobHelper.handleFail();
                return;
            }
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
            BasicExecuteParameter executeParameter = BasicExecuteParameter.builder()
                    .variable(parameter.getVariable())
                    .namedParameter(parameter.getNamedParameter())
                    .logLevel(parameter.getLogLevel())
                    .build();
            Job job =this.kettleService.executeDbJob(
                    parameter.getKettleDatabaseRepositoryProperties(),
                    parameter.getJobName(),
                    parameter.getDirectoryName(),
                    executeParameter,
                    null
            );
            XxlJobHelper.log(KettleLogStore.getAppender().getBuffer().toString());
            KettleLogStore.getAppender().clear();
            if (job.getErrors() > 1) {
                XxlJobHelper.handleFail();
                return;
            }
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            XxlJobHelper.log(e);
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}
