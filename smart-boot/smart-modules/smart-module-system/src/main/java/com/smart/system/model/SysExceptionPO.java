package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* sys_exception - 系统异常信息
* @author GCCodeGenerator
* 2022年6月10日
*/
@Getter
@Setter
@TableName("sys_exception")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysExceptionPO extends BaseModel {

    @Serial
    private static final long serialVersionUID = 7787263755804968717L;
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

    /**
    * create_time - 创建时间
    */
    private LocalDateTime createTime;

    /**
    * user_feedback - 用户是否反馈
    */
    private Boolean userFeedback;

    /**
    * feedback_message - 用户反馈信息
    */
    private String feedbackMessage;

    /**
    * feedback_time - 反馈时间
    */
    private LocalDateTime feedbackTime;

    /**
    * resolved - 是否已处理
    */
    private Boolean resolved;

    /**
    * resolved_message - 处理信息
    */
    private String resolvedMessage;

    /**
    * resolved_user_id - 处理人员ID
    */
    private Long resolvedUserId;

    /**
    * resolved_time - 处理时间
    */
    private LocalDateTime resolvedTime;

}
