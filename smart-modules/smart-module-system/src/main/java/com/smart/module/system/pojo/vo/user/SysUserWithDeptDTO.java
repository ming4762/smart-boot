package com.smart.module.system.pojo.vo.user;

import com.smart.module.system.model.SysUserPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

/**
 * 查询用户带部门信息
 * @author shizhongming
 * 2025/3/7 21:14
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class SysUserWithDeptDTO extends SysUserPO {

    @Serial
    private static final long serialVersionUID = -1690720419452533807L;

    private List<Long> deptIdList;
}
