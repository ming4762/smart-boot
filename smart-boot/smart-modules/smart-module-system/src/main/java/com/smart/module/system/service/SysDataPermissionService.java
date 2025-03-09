package com.smart.module.system.service;

import com.smart.framework.commons.core.data.Tree;
import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.SysDataPermissionPO;
import com.smart.module.system.pojo.vo.datapermission.SysDataPermissionListVO;

import java.util.List;

/**
* sys_data_permission - 数据权限表 Service
* @author SmartCodeGenerator
* 2025年3月7日 19:29:00
*/
public interface SysDataPermissionService extends BaseService<SysDataPermissionPO> {

    /**
     * 查询所有数据权限列表
     * @return 数据权限列表
     */
    List<Tree<SysDataPermissionListVO>> listAllWithFunction();
}