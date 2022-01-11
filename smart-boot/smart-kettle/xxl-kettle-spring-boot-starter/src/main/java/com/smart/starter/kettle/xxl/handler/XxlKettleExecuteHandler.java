package com.smart.starter.kettle.xxl.handler;

import com.smart.commons.core.utils.JsonUtils;
import com.smart.kettle.core.service.KettleService;
import com.smart.starter.kettle.xxl.pojo.dto.KettleJobExecuteDTO;
import com.smart.starter.kettle.xxl.pojo.dto.KettleTransExecuteDTO;
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
            Trans trans = this.kettleService.executeDbTransfer(
                    parameter.getKettleDatabaseRepositoryProperties(),
                    parameter.getTransName(),
                    parameter.getDirectoryName(),
                    parameter.getParams().toArray(new String[0]),
                    parameter.getVariableMap(),
                    parameter.getParameterMap(),
                    parameter.getLogLevel(),
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
            Job job =this.kettleService.executeDbJob(
                    parameter.getKettleDatabaseRepositoryProperties(),
                    parameter.getJobName(),
                    parameter.getDirectoryName(),
                    parameter.getVariableMap(),
                    parameter.getParameterMap(),
                    parameter.getLogLevel(),
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
