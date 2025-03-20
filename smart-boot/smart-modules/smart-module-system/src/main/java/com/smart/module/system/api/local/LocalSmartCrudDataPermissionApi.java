package com.smart.module.system.api.local;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.dto.auth.AuthRole;
import com.smart.module.api.crud.SmartCrudDataPermissionApi;
import com.smart.module.api.crud.module.SmartDataContextUserModel;
import com.smart.module.api.crud.module.SmartDataPermissionModel;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.SysDeptDTO;
import com.smart.module.api.system.parameter.SysUserDeptParameter;
import com.smart.module.system.service.SysDataPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据权限API
 * @author shizhongming
 * 2025/3/17 20:01
 * @since 5.0.0
 */
@Component
@Primary
@RequiredArgsConstructor
public class LocalSmartCrudDataPermissionApi implements SmartCrudDataPermissionApi {

    private static final String ROLE_PARAMETER = "roleIds";
    private static final String CODE_PARAMETER = "codeList";
    /**
     * 查询数据权限SQL
     */
    private static final String USER_DATA_PERMISSION_SQL = """
            select permission_code,
                   permission_column 'column',
                   scope,
                   permission_value,
                   table_name,
                   A.mapper_statement_id
            from sys_data_permission A
            join sys_role_data_permission B on A.id = B.data_permission_id
            where A.use_yn = 1
            and B. role_id in (:roleIds)
            """;

    private static final String DATA_PERMISSION_CODE_SQL = """
            select permission_code,
                   permission_column 'column',
                   scope,
                   permission_value,
                   table_name,
                   A.mapper_statement_id
            from sys_data_permission A
            where A.use_yn = 1
            and A.permission_code in (:codeList)
            """;

    private final ObjectProvider<SysUserApi> sysUserApi;
    private final ObjectProvider<SysDataPermissionService> sysDataPermissionService;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Override
    public SmartDataContextUserModel getUserContext() {
        RestUserDetails currentUser = AuthUtils.getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        return SmartDataContextUserModel.builder()
                .token(currentUser.getToken())
                .userId(currentUser.getUserId())
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .tenantId(currentUser.getUserTenant().getTenantId())
                .tenantCode(currentUser.getUserTenant().getTenantCode())
                .isSuperAdmin(AuthUtils.isSuperAdmin())
                .build();
    }

    /**
     * 获取用户部门列表
     *
     * @return 用户部门列表
     */
    @Override
    public List<Long> getUserDeptList() {
        return this.sysUserApi.getObject().listUserDept(
                        SysUserDeptParameter.builder()
                                .userId(AuthUtils.getCurrentUserId())
                                .build()
                ).stream()
                .map(SysDeptDTO::getDeptId)
                .toList();
    }

    /**
     * 获取用户部门及子部门列表
     *
     * @return 用户部门及子部门列表
     */
    @Override
    public List<Long> getUserDeptChildren() {
        return this.sysUserApi.getObject().listUserDeptWithChildren(
                        SysUserDeptParameter.builder()
                                .userId(AuthUtils.getCurrentUserId())
                                .build()
                ).stream()
                .map(SysDeptDTO::getDeptId)
                .toList();
    }

    /**
     * 获取当前用户所有数据权限
     *
     * @return 数据权限
     */
    @Override
    public List<SmartDataPermissionModel> getCurrentUserDataPermission() {
        RestUserDetails currentUser = AuthUtils.getCurrentUser();
        if (currentUser == null || CollectionUtils.isEmpty(currentUser.getRoles())) {
            // 用户未登录或用户没有权限返回null
            return Collections.emptyList();
        }
        Set<Long> roleIds = currentUser.getRoles().stream().map(AuthRole::getRoleId).collect(Collectors.toSet());
        return this.jdbcTemplate.query(USER_DATA_PERMISSION_SQL, Map.of(ROLE_PARAMETER, roleIds), new BeanPropertyRowMapper<>(SmartDataPermissionModel.class));
    }

    /**
     * 根据code获取数据权限
     *
     * @param codeList 数据权限编码
     * @return 数据权限
     */
    @Override
    public List<SmartDataPermissionModel> getDataPermissionByCode(List<String> codeList) {
        if (CollectionUtils.isEmpty(codeList)) {
            return Collections.emptyList();
        }
        return this.jdbcTemplate.query(DATA_PERMISSION_CODE_SQL, Map.of(CODE_PARAMETER, codeList), new BeanPropertyRowMapper<>(SmartDataPermissionModel.class));
    }
}
