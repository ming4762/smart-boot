package com.smart.module.auth.filter;

import com.smart.framework.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.framework.auth.core.userdetails.RestUserDetails;
import com.smart.framework.auth.core.utils.AuthUtils;
import com.smart.framework.commons.core.dto.auth.UserTenantDTO;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.tenant.SmartTenantHolder;
import com.smart.framework.commons.core.utils.IpUtils;
import com.smart.module.api.auth.AuthApi;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 租户切换拦截器
 * @author shizhongming
 * 2024/6/25 20:30
 * @since 3.0.0
 */
public class SmartAuthTenantChangeFilter extends AbstractAuthenticationProcessingFilter {

    private static final String TENANT_ID_KEY = "tenantId";
    private final AuthApi authApi;


    public SmartAuthTenantChangeFilter(String defaultFilterProcessesUrl, AuthApi authApi) {
        super(defaultFilterProcessesUrl);
        this.authApi = authApi;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 获取租户ID
        String tenantStr = request.getParameter(TENANT_ID_KEY);
        if (!StringUtils.hasText(tenantStr)) {
            throw new SystemException("切换租户失败，租户ID不存在");
        }
        Long tenantId = Long.valueOf(tenantStr);

        // 获取当前登录用户
        RestUserDetails currentUser = AuthUtils.getCurrentUser();
        if (currentUser == null) {
            // TODO:国际化
            throw new BadCredentialsException("用户未登录，无法切换租户");
        }

        // TODO:这段代码是否可以优化？
        UserTenantDTO tenant = new UserTenantDTO();
        tenant.setTenantId(tenantId);
        SmartTenantHolder.set(tenant);

        // 构建登录token
        RestUsernamePasswordAuthenticationToken loginToken = new RestUsernamePasswordAuthenticationToken(
                currentUser.getUsername(),
                currentUser.getPassword(),
                currentUser.getBindIp(),
                IpUtils.getIpAddr(request),
                currentUser.getLoginType()
        );
        // 执行登录
        Authentication authenticate = this.getAuthenticationManager().authenticate(loginToken);
        // 移除原有token
        this.authApi.offlineByToken(currentUser.getToken());
        return authenticate;
    }
}
