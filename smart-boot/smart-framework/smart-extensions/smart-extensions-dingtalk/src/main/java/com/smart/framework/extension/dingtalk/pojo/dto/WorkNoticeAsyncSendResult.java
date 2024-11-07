package com.smart.framework.extension.dingtalk.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author shizhongming
 * 2024/4/28 17:18
 * @since 3.0.0
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkNoticeAsyncSendResult extends AbstractDingtalkResult{
    @Serial
    private static final long serialVersionUID = -9178181246905744766L;

    /**
     * 创建的异步发送任务ID
     */
    @JsonProperty("task_id")
    private String taskId;
}
