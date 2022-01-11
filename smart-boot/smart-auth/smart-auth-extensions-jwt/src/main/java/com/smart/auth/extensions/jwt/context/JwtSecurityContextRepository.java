package com.smart.auth.extensions.jwt.context;

import com.smart.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.auth.extensions.jwt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ShiZhongMing
 * 2021/12/28
 * @since 1.0
 */
@Slf4j
public class JwtSecurityContextRepository implements SecurityContextRepository {

    private JwtResolver jwtResolver;


    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String jwt = JwtUtils.getJwt(request);
        if (StringUtils.isBlank(jwt)) {
            return generateNewContext();
        }
        // 解析jwt
        try {
            RestUserDetails user = jwtResolver.resolver(jwt, request);
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
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String jwt = JwtUtils.getJwt(request);
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
    public void setJwtResolver(JwtResolver jwtResolver) {
        this.jwtResolver = jwtResolver;
    }
}
