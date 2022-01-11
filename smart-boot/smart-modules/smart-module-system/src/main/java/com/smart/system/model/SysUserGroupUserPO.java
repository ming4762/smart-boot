package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import lombok.*;

/**
 * @author jackson
 * 2020/1/24 3:02 下午
 */
@TableName("sys_user_group_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserGroupUserPO extends BaseModelCreateUserTime {
    @TableField(exist = false)
    private static final long serialVersionUID = 6003195650534799142L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.NONE)
    private Long userId;

    /**
     * 用户组ID
     */
    private Long userGroupId;

    /**
     * 是否启用
     */
    private Boolean useYn;
}
