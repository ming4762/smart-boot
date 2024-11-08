package com.smart.framework.auth.extensions.jwt.context;

import com.smart.framework.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.framework.auth.core.token.TokenRepository;
import com.smart.framework.auth.core.userdetails.RestUserDetails;
import com.smart.framework.auth.core.utils.TokenUtils;
import com.smart.framework.auth.extensions.jwt.token.JwtTokenRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.List;


/**
 * @author ShiZhongMing
 * 2021/12/28
 * @since 1.0
 */
@Slf4j
public class JwtSecurityContextRepository implements SecurityContextRepository {

    private List<JwtTokenRepository> repositoryList;


    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String jwt = TokenUtils.getToken(request);
        if (!org.springframework.util.StringUtils.hasText(jwt)) {
            return generateNewContext();
        }
        // 解析jwt
        try {
            RestUserDetails user = null;
            for (JwtTokenRepository repository : this.repositoryList) {
                user = repository.getUser(jwt);
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
        RestUserDetails user = (RestUserDetails) context.getAuthentication().getPrincipal();
        String token = null;
        for (TokenRepository tokenRepository : repositoryList) {
            token = tokenRepository.save(user);
            if (org.springframework.util.StringUtils.hasText(token)) {
                break;
            }
        }
        user.setToken(token);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String jwt = TokenUtils.getToken(request);
        return StringUtils.isNotBlank(jwt);
    }

    /**
     * By default, calls {@link SecurityContextHolder#createEmptyContext()} to obtain a
     * new context (there should be no context present in the holder when this method is
     * called). Using this approach the context creation strategy is decided by the
     * return a new <tt>SecurityContextImpl</tt>.
     * @return a new SecurityContext instance. Never null.
     */
    protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }

    @Autowired
    public void setRepositoryList(List<JwtTokenRepository> repositoryList) {
        this.repositoryList = repositoryList;
    }
}
