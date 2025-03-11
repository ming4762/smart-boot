package com.smart.module.system.pojo.vo.datapermission;

import com.smart.module.system.model.SysDataPermissionPO;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 查询数据权限列表
 * @author shizhongming
 * 2025/3/9 13:16
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysDataPermissionListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6732657067193454727L;
    
    private Long dataId;

    private Long parentId;

    private String name;
    
    private Boolean withDataPermission;

    /**
     * 是否是数据权限
     * 否：功能菜单
     */
    private Boolean isDataPermission;

    /**
     * 数据权限范围
     */
    private String dataPermissionScope;

    /**
     * 数据权限列表
     */
    private List<SysDataPermissionPO> dataPermissionList;
}
