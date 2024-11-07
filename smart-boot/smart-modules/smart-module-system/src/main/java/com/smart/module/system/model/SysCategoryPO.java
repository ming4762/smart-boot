package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableTenantField;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.SqlCommandType;

import java.io.Serial;

/**
* sys_category - 分类字段
* @author SmartCodeGenerator
* 2023-1-21 21:32:15
*/
@Getter
@Setter
@TableName("sys_category")
public class SysCategoryPO extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = -985761604588742451L;

    private Long id;

    private Long parentId;

    /**
    * category_code - 分类编码
    */
    private String categoryCode;

    /**
    * category_name - 分类名称
    */
    private String categoryName;

    /**
    * seq - seq
    */
    private Integer seq;

    private String remark;

    private Boolean hasChild;

    /**
     * 是否平台通用
     */
    private Boolean tenantCommonYn;


    /**
     * 查询逻辑单独写
     */
    @TableTenantField(ignoreCommands = SqlCommandType.SELECT, platformTenantIgnoreCommands = SqlCommandType.INSERT)
    private Long tenantId;
}