package com.smart.monitor.server.manager.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 应用-用户组关系表
 * @author ShiZhongMing
 * 2022/2/9
 * @since 2.0.0
 */
@Getter
@Setter
@TableName("monitor_user_group_application")
public class MonitorUserGroupApplicationPO extends BaseModelCreateUserTime {
    private static final long serialVersionUID = -625402630296588435L;

    private Long userGroupId;

    @TableId
    private Long applicationId;
}
