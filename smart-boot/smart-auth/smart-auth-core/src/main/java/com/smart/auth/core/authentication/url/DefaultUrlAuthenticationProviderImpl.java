package com.smart.auth.core.authentication.url;

import com.smart.auth.core.annotation.NonUrlCheck;
import com.smart.auth.core.beans.UrlMappingProvider;
import com.smart.auth.core.constants.RoleEnum;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.beans.AbstractBeanNameProvider;
import com.smart.module.api.system.dto.Permission;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2021/1/4 17:03
 * @since 1.0
 */
public class DefaultUrlAuthenticationProviderImpl extends AbstractBeanNameProvider implements UrlAuthenticationProvider {

    private final UrlMappingProvider urlMappingProvider;

    public DefaultUrlAuthenticationProviderImpl(UrlMappingProvider urlMappingProvider) {
        this.urlMappingProvider = urlMappingProvider;
    }

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        // 判断是否需要校验URL
        boolean check = this.hasCheck(request);
        if (!check) {
            return true;
        }
        boolean hasPermission = false;
        Object userInfo = authentication.getPrincipal();
        RestUserDetails restUserDetails = (RestUserDetails) userInfo;
        if (restUserDetails.getRoles().contains(RoleEnum.ROLE_SUPERADMIN.getRole())) {
            return true;
        }
        Set<Permission> permissionList = restUserDetails.getPermissions();
        if (ObjectUtils.isNotEmpty(permissionList)) {
            for (Permission permission : permissionList) {
                if (StringUtils.isNotBlank(permission.getUrl())) {
                    AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(permission.getUrl(), Optional.ofNullable(permission.getMethod()).map(HttpMethod::name).orElse(null));
                    if (antPathMatcher.matches(request)) {
                        hasPermission = true;
                        break;
                    }
                }
            }
        }
        return hasPermission;
    }

    /**
     * 判断请求是否需要校验
     * @param request 请求信息
     * @return true or false
     */
    private boolean hasCheck(HttpServletRequest request) {
        // 获取请求映射
        final UrlMappingProvider.UrlMapping urlMapping = this.urlMappingProvider.matchMapping(request);
        if (Objects.isNull(urlMapping)) {
            return false;
        }
        NonUrlCheck nonUrlCheck = AnnotationUtils.findAnnotation(urlMapping.getHandlerMethod().getMethod(), NonUrlCheck.class);
        if (Objects.isNull(nonUrlCheck)) {
            nonUrlCheck = AnnotationUtils.findAnnotation(urlMapping.getHandlerMethod().getBeanType(), NonUrlCheck.class);
        }
        return Objects.isNull(nonUrlCheck);
    }
}
