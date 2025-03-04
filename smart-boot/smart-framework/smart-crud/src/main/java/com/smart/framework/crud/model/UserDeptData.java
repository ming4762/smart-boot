package com.smart.framework.crud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** 用户部门数据
 * @author shizhongming
 * 2025/3/4 19:25
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
public class UserDeptData {

    private Long userId;

    private Long deptId;

    private String deptName;

}
