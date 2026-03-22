package com.smart.module.sso.server.common.manager.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.smart.module.sso.server.common.manager.mapper.SsoOauth2ClientMapper;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientPO;
import org.springframework.stereotype.Component;

/**
 * oauth2客户端repository
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-21 21:40
 * @since 5.0.0
 */
@Component
public class SsoOauth2ClientRepository extends CrudRepository<SsoOauth2ClientMapper, SsoOauth2ClientPO> {
}
