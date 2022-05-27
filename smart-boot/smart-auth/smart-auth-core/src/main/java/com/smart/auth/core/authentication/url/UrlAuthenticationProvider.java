package com.smart.auth.core.authentication.url;

import com.smart.commons.core.beans.BeanNameProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.security.core.Authentication;


/**
 * URl验证接口
 * @author ShiZhongMing
 * 2021/1/4 16:59
 * @since 1.0
 */
public interface UrlAuthenticationProvider extends BeanNameAware, BeanNameProvider {

    /**
     * 是否拥有权限
     * @param request request
     * @param authentication authentication
     * @return 是否拥有权限
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
