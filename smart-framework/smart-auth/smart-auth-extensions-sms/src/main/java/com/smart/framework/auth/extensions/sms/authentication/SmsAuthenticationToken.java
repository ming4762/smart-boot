package com.smart.framework.auth.extensions.sms.authentication;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.core.authentication.AbstractEnhanceAuthenticationToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

/**
 * SMS认证信息
 * @author ShiZhongMing
 * 2021/6/3 13:14
 * @since 1.0
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class SmsAuthenticationToken extends AbstractEnhanceAuthenticationToken {
    @Serial
    private static final long serialVersionUID = -5507214988425089305L;

    public SmsAuthenticationToken(Serializable principal, Serializable credentials) {
        this(principal, credentials, null);
    }

    public SmsAuthenticationToken(Serializable principal, Serializable credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(AuthTypeEnum.SMS, principal, credentials, authorities);
    }

}
