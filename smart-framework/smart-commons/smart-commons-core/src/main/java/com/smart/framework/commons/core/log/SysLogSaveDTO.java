package com.smart.framework.commons.core.log;

import lombok.*;

import java.io.Serializable;

/**
 * 系统日志保存DTO
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-13 19:28
 * @since 1.0.0
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
    private Integer statusCode;

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

    private String ident;

    private Long createUserId;

    private String createBy;

    private Long tenantId;
}
