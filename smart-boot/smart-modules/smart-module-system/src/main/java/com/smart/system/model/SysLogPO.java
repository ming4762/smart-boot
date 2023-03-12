package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.log.LogSourceEnum;
import com.smart.crud.model.BaseModelCreateUserTime;
import com.smart.module.api.system.constants.LogIdentEnum;
import com.smart.system.mybatis.type.LogSourceTypeHandler;
import lombok.*;

import java.io.Serial;

/**
 * 系统日志PO类
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0
 */
@TableName(value = "sys_log", autoResultMap = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLogPO extends BaseModelCreateUserTime {
    @Serial
    private static final long serialVersionUID = 598505043338521702L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long logId;

    /**
     * 操作
     */
    private String operation;

    /**
     * 日志标识位
     */
    private LogIdentEnum ident;

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
    @TableField(typeHandler = LogSourceTypeHandler.class)
    private LogSourceEnum logSource;
}
