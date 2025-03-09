package com.smart.module.system.pojo.dbo.datapermission;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 查询数据权限列表
 * @author shizhongming
 * 2025/3/9 13:16
 * @since 5.0.0
 */
@Getter
@Setter
public class SysDataPermissionListBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6732657067193454727L;
    
    private Long functionId;

    private Long parentId;

    private Long functionName;
    
    private Long dataPermissionId;
}
