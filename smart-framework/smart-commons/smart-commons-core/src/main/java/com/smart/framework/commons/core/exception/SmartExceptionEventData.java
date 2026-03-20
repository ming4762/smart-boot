package com.smart.framework.commons.core.exception;

import lombok.*;

import java.io.Serializable;

/**
 * 系统异常事件数据
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-17 22:48
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmartExceptionEventData implements Serializable {

    /**
     * 异常编码
     */
    private Long exceptionNo;
    /**
     * 堆栈信息
     */
    private String stackTrace;

    /**
     * exception_message - 异常信息
     */
    private String exceptionMessage;

    /**
     * request_ip - 请求IP
     */
    private String requestIp;

    /**
     * server_ip - 服务器IP
     */
    private String serverIp;

    /**
     * request_path - 请求路径
     */
    private String requestPath;

    /**
     * operate_user_id - 操作人员ID
     */
    private Long operateUserId;

    /**
     * 操作人员名字
     */
    private String operationBy;
}
