package com.smart.module.api.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部门信息DTO
 * @author shizhongming
 * 2025/3/7 19:08
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class SysDeptDTO {

    private Long deptId;

    /**
     * dept_code - 部门编码
     */
    private String deptCode;

    /**
     * dept_name - 部门名称
     */
    private String deptName;

    /**
     * email - 邮箱
     */
    private String email;

    /**
     * director - 负责人
     */
    private String director;

    /**
     * phone - 电话
     */
    private String phone;
}
