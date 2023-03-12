package com.smart.module.api.system.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysExceptionSaveDTO implements Serializable {

    /**
     * id - id
     */
    private Long id;
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
