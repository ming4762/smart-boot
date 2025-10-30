package com.smart.module.system.pojo.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 保存用户信息DTO 带部门信息
 * @author shizhongming
 * 2025/3/7 20:17
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class UserSaveUpdateWithDeptDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5130819953765895017L;

    private List<Long> deptIdList;

    /**
     * 用户ID
     */
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
     * 时区
     */
    private String timezone;

    /**
     * 序号
     */
    private Integer seq;
}
