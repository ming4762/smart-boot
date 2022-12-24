package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SmartAuthLicensePO;

/**
* smart_auth_license - 许可证管理 Service
* @author SmartodeGenerator
* 2022-12-17 14:31:50
*/
public interface SmartAuthLicenseService extends BaseService<SmartAuthLicensePO> {

    /**
     * 生成license
     * @param id id
     * @return 是否生成成功
     */
    boolean generator(Long id);
}
