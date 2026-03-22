package com.smart.module.sso.server.common.manager.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.smart.module.sso.server.common.manager.mapper.SsoOauth2ClientUserMapper;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientUserPO;
import org.springframework.stereotype.Component;

/**
 * oauth2_client_user - oauth2客户端用户
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-21 21:41
 * @since 5.0.0
 */
@Component
public class SsoOauth2ClientUserRepository extends CrudRepository<SsoOauth2ClientUserMapper, SsoOauth2ClientUserPO> {
}
