package com.smart.module.auth.server.manager.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.auth.server.manager.model.Oauth2ClientPO;

/**
* oauth2_client - oauth2客户端 Service
* @author SmartCodeGenerator
* 2025年11月14日 16:54:04
*/
public interface Oauth2ClientService extends BaseService<Oauth2ClientPO> {

    /**
     * 根据clientId查询oauth2客户端
     * @param clientId 客户端ID
     * @return oauth2客户端
     */
    Oauth2ClientPO getByClientId(String clientId);

    /**
     * 根据id和useYn查询oauth2客户端
     * @param id 客户端ID
     * @return oauth2客户端
     */
    Oauth2ClientPO getByIdInUse(Long id);

}