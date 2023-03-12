package com.smart.starter.log.model;

import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.log.LogSourceEnum;
import com.smart.module.api.system.constants.LogIdentEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 日志实体类
 * @author jackson
 * 2020/1/22 2:19 下午
 */
@Getter
@Setter
@Builder
public class SysLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 7634433741019323407L;

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
}
