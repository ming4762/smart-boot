package com.smart.module.system.service.auth;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.auth.SysAuthAccessSecretPO;
import com.smart.module.system.pojo.dto.auth.SmartAuthAccessTestDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
* sys_auth_access_secret -  Service
* @author SmartCodeGenerator
* 2023年10月26日 下午7:25:26
*/
public interface SysAuthAccessSecretService extends BaseService<SysAuthAccessSecretPO> {

    /**
     * 测试访问权限
     * @param request 请求
     * @param parameter 测试参数
     * @return 是否通过
     */
    String testAccessSecret(HttpServletRequest request, SmartAuthAccessTestDTO parameter);
}