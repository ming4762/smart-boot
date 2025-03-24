package com.smart.module.system.service.auth;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.auth.SysAuthAccessSecretPO;
import com.smart.module.system.pojo.dto.access.SysAccessCreateSignDTO;

/**
* sys_auth_access_secret -  Service
* @author SmartCodeGenerator
* 2023年10月26日 下午7:25:26
*/
public interface SysAuthAccessSecretService extends BaseService<SysAuthAccessSecretPO> {

    /**
     * 创建签名
     * @param parameter 签名参数
     * @return 签名
     */
    String createSign(SysAccessCreateSignDTO parameter);
}