package com.smart.system.pojo.vo.user;

import com.smart.crud.datapermission.DataPermissionScope;
import com.smart.system.model.SysUserPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "请选择部门")
    private Long deptId;

    @NotEmpty(message = "请选择数据权限")
    private List<DataPermissionScope> dataScopeList;
}
