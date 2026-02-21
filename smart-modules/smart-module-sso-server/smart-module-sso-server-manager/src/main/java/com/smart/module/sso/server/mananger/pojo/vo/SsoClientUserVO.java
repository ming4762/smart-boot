package com.smart.module.sso.server.mananger.pojo.vo;

import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientUserPO;
import lombok.*;

import java.io.Serializable;

/**
 * SSO客户端用户VO
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-20 23:50
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SsoClientUserVO implements Serializable {

    private SysUserDTO user;

    private SsoOauth2ClientUserPO clientUser;
}
