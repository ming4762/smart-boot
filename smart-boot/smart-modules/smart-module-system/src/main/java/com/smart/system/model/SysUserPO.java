package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.crud.model.BaseModelUserTime;
import com.smart.crud.model.BaseUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * 用户实体类
 * @author jackson
 * 2020/1/22 7:04 下午
 */
@TableName("sys_user")
@Getter
@Setter
@ToString
public class SysUserPO extends BaseModelUserTime implements BaseUser {

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
     * 用户类型（10：系统用户，20：业务用户）
     */
    private String userType;

    /**
     * 序号
     */
    private Integer seq;

    private Boolean deleteYn;

    private Boolean useYn;

}
