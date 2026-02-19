package com.smart.module.sso.server.common.manager.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientPO;

/**
* oauth2_client - oauth2客户端 Service
* @author SmartCodeGenerator
* 2025年11月14日 16:54:04
*/
public interface SsoOauth2ClientService extends BaseService<SsoOauth2ClientPO> {

    /**
     * 根据clientId查询oauth2客户端
     * @param clientId 客户端ID
     * @return oauth2客户端
     */
    SsoOauth2ClientPO getByClientId(String clientId);

    /**
     * 根据id和useYn查询oauth2客户端
     * @param id 客户端ID
     * @return oauth2客户端
     */
    SsoOauth2ClientPO getByIdInUse(Long id);

}