package com.smart.system.pojo.dto.user;

import com.smart.crud.datapermission.DataPermissionScope;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 用户数据权限更新DTO
 * @author ShiZhongMing
 * 2022/10/19
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class UserDataScopeUpdateDTO implements Serializable {
    private static final long serialVersionUID = 5351950522094720322L;

    private Long userId;

    private Long deptId;

    private List<DataPermissionScope> dataScopeList;
}
