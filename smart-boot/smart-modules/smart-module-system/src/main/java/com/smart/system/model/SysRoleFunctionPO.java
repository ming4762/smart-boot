package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2020/9/22 8:45 下午
 */
@Getter
@Setter
@TableName("sys_role_function")
public class SysRoleFunctionPO extends BaseModelCreateUserTime {
    @Serial
    private static final long serialVersionUID = -8303994182948294185L;

    @TableId(type = IdType.NONE)
    private Long roleId;

    private Long functionId;

    private Boolean halfYn;
}
