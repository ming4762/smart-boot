package com.smart.crud.datapermission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2022/10/17
 * @since 3.0.0
 */
@Getter
@AllArgsConstructor
public class UserDataScope implements Serializable {
    private static final long serialVersionUID = 7039352615243076821L;

    private Long userId;

    private Long deptId;

    @NonNull
    private Set<DataPermissionScope> scopes;
}
