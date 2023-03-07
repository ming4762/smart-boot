package com.smart.auth.extensions.jwt.handler;

import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.handler.DefaultAuthSuccessDataHandler;
import com.smart.auth.core.model.LoginResult;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.RestUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/3/1 10:00
 * @since 1.0
 */
public class JwtAuthSuccessDataHandler extends DefaultAuthSuccessDataHandler implements ApplicationContextAware {

    private List<TokenRepository> tokenRepositoryList;

    @Override
    public LoginResult successData(Authentication authentication, HttpServletRequest request, LoginTypeEnum loginType) {
        RestUserDetails user = (RestUserDetails)authentication.getPrincipal();
        String token = this.save(user);
        // 设置token
        user.setToken(token);
        return super.successData(authentication, request, loginType);
    }

    private String save(@NonNull RestUserDetails user) {
        String result = null;
        for (TokenRepository tokenRepository : tokenRepositoryList) {
            result = tokenRepository.save(user);
            if (StringUtils.hasText(result)) {
                break;
            }
        }
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.tokenRepositoryList = Arrays.stream(applicationContext.getBeanNamesForType(TokenRepository.class))
                .map(item -> applicationContext.getBean(item, TokenRepository.class))
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .toList();
    }
}
