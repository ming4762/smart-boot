package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_system_user - 
* @author SmartCodeGenerator
* 2023-1-23 20:02:16
*/
@Getter
@Setter
@TableName("sys_system_user")
public class SysSystemUserPO extends BaseModelCreateUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * user_id - userId
    */
    private Long userId;

    /**
    * system_id - systemId
    */
    private Long systemId;

}