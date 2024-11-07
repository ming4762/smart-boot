package com.smart.module.system.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.SysDeptPO;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
* sys_dept - 部门表 Service
* @author GCCodeGenerator
* 2022年10月13日 上午10:24:21
*/
public interface SysDeptService extends BaseService<SysDeptPO> {

    /**
     * 查询所有下级
     * @param parentIds 上级ID
     * @return 下级ID
     */
    @NonNull
    Set<Long> queryAllChildIds(@NonNull Set<Long> parentIds);
}
