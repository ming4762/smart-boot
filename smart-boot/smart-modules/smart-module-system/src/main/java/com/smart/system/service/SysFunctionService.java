package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysFunctionPO;
import com.smart.system.pojo.vo.function.SysFunctionVO;

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
}
