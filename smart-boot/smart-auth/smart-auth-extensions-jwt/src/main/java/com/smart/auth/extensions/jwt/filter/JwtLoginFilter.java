package com.smart.auth.extensions.jwt.filter;

import com.smart.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.auth.core.model.LoginParameter;
import com.smart.auth.extensions.jwt.context.JwtContext;
import com.smart.commons.core.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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

    @Override
    @Autowired
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * 重写afterPropertiesSet，不做任何事情
     * 默认的函数会检测AuthenticationManager是否存在，通过{@link org.springframework.security.config.annotation.configuration.AutowireBeanFactoryObjectPostProcessor#postProcess}进行注入会报错
     * 原因是AutowireBeanFactoryObjectPostProcessor创建bean的顺序和spring不一致
     * AutowireBeanFactoryObjectPostProcessor是先 initializeBean 然后 autowireBean
     * 正常应该是 autowireBean 然后 initializeBean
     */
    @Override
    public void afterPropertiesSet() {
        // do nothing
    }
}
