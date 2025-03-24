package com.smart.framework.auth.extensions.jwt.context;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.framework.auth.core.utils.TokenUtils;
import com.smart.framework.auth.extensions.jwt.token.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * @author ShiZhongMing
 * 2021/12/28
 * @since 1.0
 */
@Slf4j
public class JwtSecurityContextRepository implements SecurityContextRepository {

    private List<JwtTokenRepository> jwtTokenRepositoryList;

    
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String token = TokenUtils.getToken(request);
        if (!StringUtils.hasText(token)) {
            // 没有token
            return generateNewContext();
        }
        try {
            RestUserDetails user = null;
            for (JwtTokenRepository jwtTokenRepository : Objects.requireNonNullElseGet(this.jwtTokenRepositoryList, Collections::<JwtTokenRepository>emptyList)) {
                user = jwtTokenRepository.getUserByToken(token);
                if (user != null) {
                    break;
                }
            }
            if (user == null) {
                return generateNewContext();
            }
            RestUsernamePasswordAuthenticationToken authentication = new RestUsernamePasswordAuthenticationToken(user, null, user.getAuthorities(), user.getBindIp(), user.getLoginIp(), user.getLoginType());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContext securityContext = generateNewContext();
            securityContext.setAuthentication(authentication);
            return securityContext;
        } catch (Exception e) {
            log.warn("解析JWT失败", e);
            return generateNewContext();
        }
    }


    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // do Nothing
        // JWT 不存储信息
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String jwt = TokenUtils.getToken(request);
        return StringUtils.hasText(jwt);
    }


    protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }

    @Autowired
    public void setJwtTokenRepositoryList(List<JwtTokenRepository> jwtTokenRepositoryList) {
        this.jwtTokenRepositoryList = jwtTokenRepositoryList;
    }
}
