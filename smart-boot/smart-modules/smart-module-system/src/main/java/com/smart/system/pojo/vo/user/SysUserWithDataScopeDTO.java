package com.smart.system.pojo.vo.user;

import com.smart.crud.datapermission.DataPermissionScope;
import com.smart.system.model.SysUserPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 用户 部门 数据权限 vo
 * @author ShiZhongMing
 * 2022/10/19
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysUserWithDataScopeDTO extends SysUserPO {
    private static final long serialVersionUID = -7021384417582166698L;

    private Long deptId;

    private List<DataPermissionScope> dataScopeList;
}
