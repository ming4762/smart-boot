package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
* sys_change_log - 系统变更日志
* @author SmartCodeGenerator
* 2025年5月5日 19:12:09
*/
@Getter
@Setter
@TableName("sys_change_log")
public class SysChangeLogPO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * version - 版本号
    */
    private String version;

    /**
    * change_module - 变更模块
    */
    private String changeModule;

    /**
    * log_time - 变更时间
    */
    private ZonedDateTime logTime;

    /**
    * title - 标题
    */
    private String title;

    /**
    * change_content - 变更内容
    */
    private String changeContent;

    /**
    * change_type - 变更类型
    */
    private String changeType;

    /**
    * affected_scope - 影响范围
    */
    private String affectedScope;

    /**
    * send_system_message_yn - 是否发送系统消息
    */
    private Boolean sendSystemMessageYn;

    /**
    * system_message_id - 系统消息ID
    */
    private Long systemMessageId;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * release_yn - 是否发布
    */
    private Boolean releaseYn;

    /**
    * release_time - 发布时间
    */
    private ZonedDateTime releaseTime;

    /**
    * release_user_id - 发布人ID
    */
    private Long releaseUserId;

    /**
    * release_by - 发布人
    */
    private String releaseBy;

    /**
    * revoked_yn - 是否撤销
    */
    private Boolean revokedYn;

    /**
    * revoke_time - 撤销时间
    */
    private ZonedDateTime revokeTime;

    /**
    * revoke_user_id - 撤销人ID
    */
    private Long revokeUserId;

    /**
    * revoke_by - 撤销人
    */
    private String revokeBy;

    /**
    * revoke_remark - 撤销备注
    */
    private String revokeRemark;
}