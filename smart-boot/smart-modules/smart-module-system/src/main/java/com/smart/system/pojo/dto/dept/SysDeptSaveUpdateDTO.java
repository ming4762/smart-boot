package com.smart.system.pojo.dto.dept;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
* sys_dept - 部门表
* @author GCCodeGenerator
* 2022年10月13日 上午10:24:21
*/
@Getter
@Setter
@ToString
public class SysDeptSaveUpdateDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = 5782485832116711926L;
    /**
    * 部门ID
    */
    private Long deptId;
    /**
    * 上级ID
    */
    private Long parentId;
    /**
    * 部门编码
    */
    @NotNull(message = "部门编码不能为空")
    private String deptCode;
    /**
    * 部门类型
    */
    @NotNull(message = "请选择部门类型")
    private String deptType;
    /**
    * 部门名称
    */
    @NotNull(message = "请输入部门名称")
    private String deptName;
    /**
    * 邮箱
    */
    private String email;
    /**
    * 负责人
    */
    private String director;
    /**
    * 电话
    */
    private String phone;
    /**
    * 启用状态
    */
    private Boolean useYn;

    /**
    * 序号
    */
    private Integer seq;
    /**
    * 备注
    */
    private String remark;

}
