package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysSystemPO;
import com.smart.system.pojo.dto.system.SystemSetUserDTO;

/**
 * @author zhongming4762
 * 2023/1/21 21:21
 */
public interface SysSystemService extends BaseService<SysSystemPO> {

    /**
     * 设置关联用户信息
     * @param parameter 参数
     * @return 是否设置成功
     */
    Boolean setUser(SystemSetUserDTO parameter);
}
