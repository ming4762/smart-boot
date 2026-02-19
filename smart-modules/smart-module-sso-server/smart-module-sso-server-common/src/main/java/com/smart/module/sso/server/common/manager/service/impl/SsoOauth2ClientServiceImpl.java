package com.smart.module.sso.server.common.manager.service.impl;

import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.sso.server.common.manager.mapper.SsoOauth2ClientMapper;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientPO;
import com.smart.module.sso.server.common.manager.service.SsoOauth2ClientService;
import org.springframework.stereotype.Service;

/**
 * oauth2_client - oauth2客户端 Service实现类
 *
 * @author SmartCodeGenerator
 * 2025年11月14日 16:54:04
 */
@Service
public class SsoOauth2ClientServiceImpl extends BaseServiceImpl<SsoOauth2ClientMapper, SsoOauth2ClientPO> implements SsoOauth2ClientService {

    /**
     * 根据clientId查询oauth2客户端
     *
     * @param clientId 客户端ID
     * @return oauth2客户端
     */
    @Override
    public SsoOauth2ClientPO getByClientId(String clientId) {
        return this.lambdaQuery().eq(SsoOauth2ClientPO::getClientId, clientId)
                .eq(SsoOauth2ClientPO::getUseYn, Boolean.TRUE)
                .one();
    }

    /**
     * 根据id和useYn查询oauth2客户端
     *
     * @param id    客户端ID
     * @return oauth2客户端
     */
    @Override
    public SsoOauth2ClientPO getByIdInUse(Long id) {
        return this.lambdaQuery().eq(SsoOauth2ClientPO::getId, id)
                .eq(SsoOauth2ClientPO::getUseYn, Boolean.TRUE)
                .one();
    }
}