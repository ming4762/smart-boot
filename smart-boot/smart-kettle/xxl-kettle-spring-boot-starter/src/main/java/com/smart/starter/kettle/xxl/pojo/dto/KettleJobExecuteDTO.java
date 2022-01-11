package com.smart.starter.kettle.xxl.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * 转换执行参数
 * @author shizhongming
 * 2021/7/19 6:25 下午
 */
@Getter
@Setter
@ToString
public class KettleJobExecuteDTO extends AbstractKettleExecuteDTO {

    /**
     * 转换名字
     */
    private String jobName;

    public String getJobName() {
        Assert.notNull(this.jobName, "转换名字不能为空");
        return jobName;
    }
}
