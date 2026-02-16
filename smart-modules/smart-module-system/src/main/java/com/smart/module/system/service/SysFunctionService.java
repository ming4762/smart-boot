package com.smart.module.system.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.SysFunctionPO;
import com.smart.module.system.pojo.parameter.function.SysFunctionSaveUpdateParameter;
import com.smart.module.system.pojo.vo.function.SysFunctionVO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author jackson
 * 2020/1/27 12:15 下午
 */
public interface SysFunctionService extends BaseService<SysFunctionPO> {

    /**
     * 查询function信息
     * @param functionId id
     * @return SysFunctionListVO
     */
    SysFunctionVO getUserAndParentById(Long functionId);

    /**
     * 根据租户ID查询功能
     * @param tenantId 租户ID
     * @return 功能列表
     */
    List<SysFunctionPO> listTenantFunction(@NonNull Long tenantId);

    /**
     * 添加修改功能
     * @param parameter 参数
     * @return boolean
     */
    boolean saveUpdate(SysFunctionSaveUpdateParameter parameter);
}
