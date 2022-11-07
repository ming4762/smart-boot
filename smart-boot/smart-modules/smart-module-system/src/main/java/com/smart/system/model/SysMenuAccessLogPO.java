package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单访问记录表
 * @author ShiZhongMing
 * 2022/7/8
 * @since 3.0.0
 */
@TableName("sys_menu_access_log")
@Getter
@Setter
public class SysMenuAccessLogPO extends BaseModelCreateUserTime {
    private static final long serialVersionUID = -3804315353319882684L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long logId;

    private Long functionId;
}
