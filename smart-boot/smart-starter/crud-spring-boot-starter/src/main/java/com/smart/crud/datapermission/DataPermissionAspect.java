package com.smart.crud.datapermission;

import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.CommonQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

/**
 * 数据权限切面
 * @author ShiZhongMing
 * 2022/10/14
 * @since 3.0.0
 */
@Aspect
@Slf4j
public class DataPermissionAspect {

    private final UserDataScopeProvider provider;

    public DataPermissionAspect(UserDataScopeProvider provider) {
        this.provider = provider;
    }


    @Before("@annotation(dataPermission)")
    public void before(JoinPoint point, DataPermission dataPermission) {
        if (this.provider.isSuperAdmin()) {
            // 超级管理员不拦截
            return;
        }
        var deptAlias = dataPermission.deptAlias();
        var userAlias = dataPermission.userAlias();
        // 获取用户的数据权限范围
        var userDataScope = this.provider.getUserUserDataScope();
        if (userDataScope == null || CollectionUtils.isEmpty(userDataScope.getScopes())) {
            // 没有设置数据权限不拦截
            return;
        }
        var deptAliasSql = StringUtils.isBlank(deptAlias) ? "" : deptAlias + ".";
        var userAliasSql = StringUtils.isBlank(userAlias) ? "" : userAlias + ".";
        var sql = new StringBuilder();
        for (var scope : userDataScope.getScopes()) {
            if (scope.equals(DataPermissionScope.DATA_ALL)) {
                // 如果是所有数据权限，则不加限制
                sql = new StringBuilder();
                break;
            }
            if (scope.equals(DataPermissionScope.DATA_DEPT) && deptAlias != null) {
                // 如果是部门数据权限，则添加部门权限SQL
                sql.append(
                        String.format(" or %sdept_id = %s", deptAliasSql, userDataScope.getDeptId())
                );
            }
            if (scope.equals(DataPermissionScope.DATA_PERSONAL) && userAlias != null) {
                // 设置人员数据权限
                sql.append(
                        String.format(" or %suser_id = %s", userAliasSql, userDataScope.getUserId())
                );
            }
            if (scope.equals(DataPermissionScope.DATA_DEPT_AND_CHILD) && deptAlias != null) {
                var deptIds = this.provider.getUserAllDeptId(userDataScope.getDeptId());
                var inSql = CollectionUtils.isEmpty(deptIds) ? "-1" : deptIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                sql.append(
                        String.format(" or %sdept_id in (%s)", deptAliasSql, inSql)
                );
            }
        }
        DataPermissionHolder.clear();
        if (StringUtils.isNotBlank(sql.toString())) {
            if (dataPermission.autoInjection()) {
                DataPermissionHolder.setSql(sql.toString());
            }
            var param = point.getArgs().length > 0 ? point.getArgs()[0] : null;
            if (param instanceof CommonQuery commonQuery) {
                commonQuery.getParameter().put(CrudCommonEnum.DATA_PERMISSION.name(), sql.toString());
            }
        }
    }
}
