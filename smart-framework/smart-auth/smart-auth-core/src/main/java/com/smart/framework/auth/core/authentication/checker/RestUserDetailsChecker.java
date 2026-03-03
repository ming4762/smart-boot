package com.smart.framework.auth.core.authentication.checker;

import com.smart.framework.auth.core.exception.RestUsernameNotFoundException;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.commons.core.i18n.I18nUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 认证用户检查器
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 22:16
 * @since 5.0.0
 */
public class RestUserDetailsChecker  implements UserDetailsChecker {

    @Override
    public void check(UserDetails user) {
        if (user == null) {
            throw new RestUsernameNotFoundException(I18nUtils.get(AuthI18nMessage.USER_NOT_FOUND_ERROR));
        }
        if (!user.isAccountNonLocked()) {
            throw new LockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_LOCKED));
        }
        if (!user.isEnabled()) {
            throw new DisabledException(I18nUtils.get(AuthI18nMessage.ACCOUNT_DISABLED));
        }
        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException(I18nUtils.get(AuthI18nMessage.ACCOUNT_EXPIRED));
        }
    }
}
