package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelUserTime;
import com.smart.module.api.crud.constants.DataPermissionScopeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* sys_data_permission - 数据权限表
* @author SmartCodeGenerator
* 2025年3月7日 19:29:00
*/
@Getter
@Setter
@TableName("sys_data_permission")
public class SysDataPermissionPO extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = 1220885642172591154L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 所属功能ID
     */
    private Long functionId;

    /**
    * permission_code - 权限编码
    */
    private String permissionCode;

    /**
    * permission_name - 权限名称
    */
    private String permissionName;

    /**
    * scope - 数据权限范围
    */
    private DataPermissionScopeEnum scope;

    /**
    * permission_column - 权限字段，如果为null，则根据scope使用默认值
    */
    private String permissionColumn;

    /**
    * permission_value - 权限规则值
    */
    private String permissionValue;

    /**
    * table_name - 表名，如果为空，则根据SQL中表和字段确认
    */
    private String tableName;

    /**
    * mapper_statement_id - mapper语句id
    */
    private String mapperStatementId;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * use_yn - 是否启用
    */
    @TableUseYnField
    private Boolean useYn;

}