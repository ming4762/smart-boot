package com.smart.crud.datapermission;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

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
        var deptAlias = dataPermission.deptAlias();
        var userAlias = dataPermission.userAlias();
        // 获取用户的数据权限范围
        var userDataScope = this.provider.getCurrentUserDataScope();
        if (userDataScope.isEmpty()) {
            // todo:没有设置数据权限如何处理？
        }
        var sql = new StringBuffer();
        for (var scope : userDataScope) {
            if (scope.equals(DataPermissionScope.DATA_ALL.name())) {
                // 如果是所有数据权限，则不加限制
                sql = new StringBuffer();
                break;
            }
            if (scope.equals(DataPermissionScope.DATA_DEPT.name()) && StringUtils.isNotBlank(deptAlias)) {
                // 如果是部门数据权限，则添加部门权限SQL
                sql.append(
                        String.format(" or %s.dept_id = %s", deptAlias, this.provider.getUserDeptId())
                );
            }
            if (scope.equals(DataPermissionScope.DATA_PERSONAL.name()) && StringUtils.isNotBlank(userAlias)) {
                // 设置人员数据权限
                sql.append(
                        String.format(" or %s.user_id = %s", userAlias, this.provider.getUserId())
                );
            }
            if (scope.equals(DataPermissionScope.DATA_DEPT_AND_CHILD.name()) && StringUtils.isNotBlank(deptAlias)) {
                var deptIds = this.provider.getUserAllDeptId();
                var inSql = CollectionUtils.isEmpty(deptIds) ? "-1" : deptIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                sql.append(
                        String.format(" or %s.dept_id in (%s)", deptAlias, inSql)
                );
            }
        }
    }
}
