package com.smart.module.system.pojo.dto.datapermission;

import com.smart.framework.crud.datapermission.constants.DataPermissionScopeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
* sys_data_permission - 数据权限表
* @author SmartCodeGenerator
* 2025年3月7日 19:29:00
*/
@Getter
@Setter
@ToString
public class SysDataPermissionSaveUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4113221560580077058L;
    /**
    * id
    */
    private Long id;

    @NotNull(message = "功能id不能为空")
    private Long functionId;

    /**
    * 权限编码
    */
    @NotNull(message = "权限编码不能为空")
    private String permissionCode;
    /**
    * 权限名称
    */
    @NotNull(message = "权限名称不能为空")
    private String permissionName;
    /**
    * 数据权限范围
DATA_ALL：所有权限
DATA_DEPT：当前部门权限
DATA_DEPT_AND_CHILD：当前部门及子部门权限
DATA_PERSONAL：当前人员权限
DATA_CUSTOM：自定义权限
    */
    @NotNull(message = "权限名称不能为空")
    private DataPermissionScopeEnum scope;
    /**
    * 权限字段，如果为null，则根据scope使用默认值
    */
    private String permissionColumn;
    /**
    * 权限规则值
    */
    private String permissionValue;
    /**
    * 表名，如果为空，则根据SQL中表和字段确认
    */
    private String tableName;
    /**
    * mapper语句id
    */
    private String mapperStatementId;
    /**
    * 备注
    */
    private String remark;

}