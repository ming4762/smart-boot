package com.smart.module.auth.server.manager.service.impl;

import com.smart.framework.crud.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import com.smart.module.auth.server.manager.model.Oauth2ClientPO;
import com.smart.module.auth.server.manager.service.Oauth2ClientService;
import com.smart.module.auth.server.manager.mapper.Oauth2ClientMapper;

/**
 * oauth2_client - oauth2客户端 Service实现类
 *
 * @author SmartCodeGenerator
 * 2025年11月14日 16:54:04
 */
@Service
public class Oauth2ClientServiceImpl extends BaseServiceImpl<Oauth2ClientMapper, Oauth2ClientPO> implements Oauth2ClientService {

    /**
     * 根据clientId查询oauth2客户端
     *
     * @param clientId 客户端ID
     * @return oauth2客户端
     */
    @Override
    public Oauth2ClientPO getByClientId(String clientId) {
        return this.lambdaQuery().eq(Oauth2ClientPO::getClientId, clientId)
                .eq(Oauth2ClientPO::getUseYn, Boolean.TRUE)
                .one();
    }

    /**
     * 根据id和useYn查询oauth2客户端
     *
     * @param id    客户端ID
     * @return oauth2客户端
     */
    @Override
    public Oauth2ClientPO getByIdInUse(Long id) {
        return this.lambdaQuery().eq(Oauth2ClientPO::getId, id)
                .eq(Oauth2ClientPO::getUseYn, Boolean.TRUE)
                .one();
    }
}