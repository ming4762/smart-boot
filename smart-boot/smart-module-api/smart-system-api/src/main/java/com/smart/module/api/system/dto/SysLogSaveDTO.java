package com.smart.module.api.system.dto;

import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.log.LogSourceEnum;
import com.smart.module.api.system.constants.LogIdentEnum;
import lombok.*;

import java.io.Serializable;

/**
 * 日志保存DTO
 * @author zhongming4762
 * 2023/3/11
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysLogSaveDTO implements Serializable {

    /**
     * 操作
     */
    private String operation;

    /**
     * 用时
     */
    private Long useTime;

    /**
     * 方法
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * IP
     */
    private String ip;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 返回值
     */
    private String result;

    /**
     * 请求方式
     */
    private LogOperationTypeEnum operationType;

    private String platform;

    /**
     * 日志来源
     */
    private LogSourceEnum logSource;

    private LogIdentEnum ident;

    private Long createUserId;

    private String createBy;
}
