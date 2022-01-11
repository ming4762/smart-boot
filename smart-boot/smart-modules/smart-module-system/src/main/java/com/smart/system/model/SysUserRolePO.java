package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import lombok.*;

/**
 * @author jackson
 * 2020/1/24 3:40 下午
 */
@TableName("sys_user_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRolePO extends BaseModelCreateUserTime {

    private static final long serialVersionUID = 8767672740037372836L;
    /**
     * 用户ID
     */
    @TableId(type = IdType.NONE)
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 是否启用
     */
    private Boolean enable;
}
