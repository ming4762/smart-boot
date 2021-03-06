package com.smart.auth.core.handler;

import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.model.LoginParameter;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.RestJsonWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

/**
 * 登录成功执行器
 * @author shizhongming
 * 2021/1/1 3:49 上午
 */
public class AuthLoginSuccessHandler implements AuthenticationSuccessHandler, InitializingBean {


    private AuthProperties authProperties;

    private AuthSuccessDataHandler authSuccessDataHandler;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        final LoginParameter loginParameter = LoginParameter.create(httpServletRequest);
        this.setSessionMaxInactiveInterval(httpServletRequest, loginParameter.getLoginType());
        RestJsonWriter.writeJson(httpServletResponse, Result.success(this.authSuccessDataHandler.successData(authentication, httpServletRequest, loginParameter.getLoginType())));
    }

    /**
     * 设置session过期时间
     * @param request 请求信息
     */
    protected void setSessionMaxInactiveInterval(HttpServletRequest request, LoginTypeEnum loginType) {

        // 获取有效期
        Duration timeout = authProperties.getSession().getTimeout().getGlobal();
        if (Objects.equals(loginType, LoginTypeEnum.MOBILE)) {
            timeout = authProperties.getSession().getTimeout().getMobile();
        } else if (Objects.equals(loginType, LoginTypeEnum.REMEMBER)) {
            timeout = authProperties.getSession().getTimeout().getRemember();
        }
        request.getSession().setMaxInactiveInterval((int) timeout.getSeconds());
    }

    @Autowired
    public void setAuthProperties(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.authProperties, "authProperties is null, please init it");
        Assert.notNull(this.authProperties, "AuthSuccessDataHandler is null, please init it");
    }

    @Autowired
    public void setAuthSuccessDataHandler(AuthSuccessDataHandler authSuccessDataHandler) {
        this.authSuccessDataHandler = authSuccessDataHandler;
    }
}
