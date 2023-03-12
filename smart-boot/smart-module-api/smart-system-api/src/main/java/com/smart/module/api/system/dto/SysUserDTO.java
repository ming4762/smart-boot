package com.smart.module.api.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/3/11 19:20
 */
@Getter
@Setter
@ToString
public class SysUserDTO implements Serializable {

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

    private Boolean useYn;
}
