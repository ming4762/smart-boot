package com.smart.framework.auth.extensions.session;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import org.springframework.lang.NonNull;
import org.springframework.session.SessionIdGenerator;

import java.util.UUID;

/**
 * 基于用户信息创建session id
 * @author shizhongming
 * 2025/3/12 20:54
 * @since 5.0.0
 */
public class UserDataSessionIdGenerator implements SessionIdGenerator {

    private static final String ID_FORMATTER = "smart-session&#%s$#%s$#%s";

    @Override
    @NonNull
    public String generate() {
        RestUserDetails currentUser = AuthUtils.getCurrentUser();
        if (currentUser == null) {
            return UUID.randomUUID().toString();
        }
        String sessionId = String.format(ID_FORMATTER, currentUser.getUsername(), currentUser.getUserTenant().getTenantId(), UUID.randomUUID());
        currentUser.setToken(sessionId);
        return sessionId;
    }
}
