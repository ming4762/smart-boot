package com.smart.module.system.pojo.dto.user;

import com.smart.module.system.model.SysUserPO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * 保存用户信息DTO 带部门信息
 * @author shizhongming
 * 2025/3/7 20:17
 * @since 5.0.0
 */
@Getter
@Setter
public class UserSaveUpdateWithDeptDTO extends SysUserPO {

    @Serial
    private static final long serialVersionUID = -5130819953765895017L;

    private List<Long> deptIdList;
}
