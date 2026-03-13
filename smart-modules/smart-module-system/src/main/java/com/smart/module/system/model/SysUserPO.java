package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelDeleteUserTime;
import com.smart.framework.crud.model.BaseUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * 用户实体类
 * @author jackson
 * 2020/1/22 7:04 下午
 */
@TableName(value = "sys_user", autoResultMap = true)
@Getter
@Setter
@ToString
public class SysUserPO extends BaseModelDeleteUserTime implements BaseUser {

    @Serial
    private static final long serialVersionUID = -671533082313767123L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String fullName;

    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String mobile;

     /**
     * 用户时区
     */
    private String timezone;


    /**
     * 是否系统内置
     */
    private Boolean buildIn;

    /**
     * 序号
     */
    private Integer seq;

    @TableLogic
    private Boolean deleteYn;

    @TableUseYnField
    private Boolean useYn;

     /**
     * 钉钉用户的unionId
     */
    private String dingtalkUnionId;

    /**
     * 微信用户的unionid
     */
    private String wechatUnionId;

    /**
     * 最后登录租户ID
     */
    private Long lastLoginTenantId;
}
