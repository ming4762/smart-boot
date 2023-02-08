package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_dept - 部门表
* @author GCCodeGenerator
* 2022年10月13日 上午10:24:21
*/
@Getter
@Setter
@TableName("sys_dept")
public class SysDeptPO extends BaseModelUserTime {

    private static final long serialVersionUID = 8885082382210229000L;
    /**
    * dept_id - 部门ID
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long deptId;

    /**
    * parent_id - 上级ID
    */
    private Long parentId;

    /**
    * dept_code - 部门编码
    */
    private String deptCode;

    /**
    * dept_type - 部门类型
    */
    private String deptType;

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
    * use_yn - 启用状态
    */
    private Boolean useYn;

    /**
    * delete_yn - 删除状态
    */
    private Boolean deleteYn;

    /**
    * seq - 需要
    */
    private Integer seq;

    /**
    * phone - 电话
    */
    private String phone;

    /**
    * remark - 备注
    */
    private String remark;

    /**
     * 是否有小鸡
     */
    private Boolean hasChild;

}
