package com.smart.auth.extensions.jwt.handler;

import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.handler.DefaultAuthSuccessDataHandler;
import com.smart.auth.core.model.LoginResult;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.jwt.resolver.JwtResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/3/1 10:00
 * @since 1.0
 */
public class JwtAuthSuccessDataHandler extends DefaultAuthSuccessDataHandler implements InitializingBean, ApplicationContextAware {

    private JwtResolver jwtResolver;

    private List<TokenRepository> tokenRepositoryList;

    @Override
    public LoginResult successData(Authentication authentication, HttpServletRequest request, LoginTypeEnum loginType) {
        RestUserDetails user = (RestUserDetails)authentication.getPrincipal();
        String jwt = this.jwtResolver.create(user);
        this.save(jwt, user);
        // 设置token
        user.setToken(jwt);
        return super.successData(authentication, request, loginType);
    }

    private void save(@NonNull String jwt, @NonNull RestUserDetails user) {
        boolean result;
        for (TokenRepository tokenRepository : tokenRepositoryList) {
            result = tokenRepository.save(jwt, user);
            if (result) {
                break;
            }
        }
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.jwtResolver, "jwtResolver is null, please init it");
    }

    @Autowired
    public void setJwtResolver(JwtResolver jwtResolver) {
        this.jwtResolver = jwtResolver;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.tokenRepositoryList = Arrays.stream(applicationContext.getBeanNamesForType(TokenRepository.class))
                .map(item -> applicationContext.getBean(item, TokenRepository.class))
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .toList();
    }
}
