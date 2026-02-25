package com.smart.framework.auth.core.filter;

import com.smart.framework.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.framework.auth.core.model.LoginParameter;
import com.smart.framework.commons.core.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

/**
 * JWT登录拦截器
 * @author shizhongming
 * 2021/1/1 3:02 上午
 */
public class WebLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final Boolean bindIp;
    @Setter
    private String authDomain;

    public WebLoginFilter(String loginUrl, Boolean bindIp) {
        super(loginUrl);
        this.bindIp = bindIp;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 创建请求参数
        final LoginParameter loginParameter = LoginParameter.create(httpServletRequest);
        final RestUsernamePasswordAuthenticationToken authenticationToken = new RestUsernamePasswordAuthenticationToken(loginParameter.getUsername(), loginParameter.getPassword(), this.bindIp, IpUtils.getIpAddr(httpServletRequest), loginParameter.getLoginType());
        if (StringUtils.hasText(this.authDomain)) {
            authenticationToken.setAuthDomain(this.authDomain);
        }
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
