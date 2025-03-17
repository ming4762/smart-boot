package com.smart.framework.crud.datapermission.handler;

import com.smart.module.api.crud.module.SmartDataContextUserModel;

import java.util.List;
import java.util.function.Supplier;

/**
 * 数据权限缓存
 * @author shizhongming
 * 2025/3/7 11:06
 * @since 5.0.0
 */
public class SmartDataContextHolder {

    private SmartDataContextHolder() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<SmartDataContext> DATA_PERMISSION_MODEL = ThreadLocal.withInitial(SmartDataContext::new);

    /**
     * 获取用户信息上下文
     * @param supplier 用户信息上下文
     * @return 用户信息上下文
     */
    static SmartDataContextUserModel getUserContext(Supplier<SmartDataContextUserModel> supplier) {
        SmartDataContext smartDataContext = DATA_PERMISSION_MODEL.get();
        if (smartDataContext.userCached) {
            return smartDataContext.contextUser;
        }
        smartDataContext.userCached = true;
        smartDataContext.contextUser = supplier.get();
        return smartDataContext.contextUser;
    }

    /**
     * 获取用户部门列表
     * @param supplier 用户部门列表
     * @return 用户部门列表
     */
    static List<Long> getUserDeptList(Supplier<List<Long>> supplier) {
        SmartDataContext smartDataContext = DATA_PERMISSION_MODEL.get();
        if (smartDataContext.deptCached) {
            return smartDataContext.userDeptList;
        }
        smartDataContext.deptCached = true;
        smartDataContext.userDeptList = supplier.get();
        return smartDataContext.userDeptList;
    }

    /**
     * 获取用户部门及子部门列表
     * @param supplier 用户部门及子部门列表
     * @return 用户部门及子部门列表
     */
    static List<Long> getUserDeptWithChildren(Supplier<List<Long>> supplier) {
        SmartDataContext smartDataContext = DATA_PERMISSION_MODEL.get();
        if (smartDataContext.deptChildrenCached) {
            return smartDataContext.userDeptWithChildren;
        }
        smartDataContext.deptChildrenCached = true;
        smartDataContext.userDeptWithChildren = supplier.get();
        return smartDataContext.userDeptWithChildren;
    }

    /**
     * 清除数据权限上下文
     */
    public static void clear() {
        DATA_PERMISSION_MODEL.remove();
    }

    private static class SmartDataContext {
        private SmartDataContextUserModel contextUser;
        private List<Long> userDeptList;
        private List<Long> userDeptWithChildren;
        // 设置是否缓存，防止缓存穿透
        private boolean userCached;
        private boolean deptCached;
        private boolean deptChildrenCached;
    }
}
