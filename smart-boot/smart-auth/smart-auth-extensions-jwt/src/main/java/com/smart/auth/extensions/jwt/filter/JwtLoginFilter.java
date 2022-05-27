package com.smart.auth.extensions.jwt.filter;

import com.smart.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.auth.core.model.LoginParameter;
import com.smart.auth.extensions.jwt.context.JwtContext;
import com.smart.commons.core.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * JWT登录拦截器
 * @author shizhongming
 * 2021/1/1 3:02 上午
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {



    private final Boolean bindIp;

    public JwtLoginFilter(JwtContext jwtContext, Boolean bindIp) {
        super(jwtContext.getLoginUrl());
        this.bindIp = bindIp;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 创建请求参数
        final LoginParameter loginParameter = LoginParameter.create(httpServletRequest);
        final RestUsernamePasswordAuthenticationToken authenticationToken = new RestUsernamePasswordAuthenticationToken(loginParameter.getUsername(), loginParameter.getPassword(), this.bindIp, IpUtils.getIpAddr(httpServletRequest), loginParameter.getLoginType());
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

}
