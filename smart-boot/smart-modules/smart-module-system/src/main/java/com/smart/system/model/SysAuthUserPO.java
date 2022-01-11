package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户认证信息
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0.7
 */
@Getter
@Setter
@TableName("sys_auth_user")
public class SysAuthUserPO extends BaseModel {
    private static final long serialVersionUID = -8246582845593471040L;

    @TableId
    private Long userId;

    /**
     * 登录失败次数
     */
    private Integer loginFailTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
