package com.smart.auth.core.beans;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.smart.commons.core.beans.AbstractBeanNameProvider;
import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.i18n.I18nUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认的映射提供器
 * @author ShiZhongMing
 * 2021/3/8 17:32
 * @since 1.0
 */
public class DefaultUrlMappingProvider extends AbstractBeanNameProvider implements InitializingBean, UrlMappingProvider {

    private final Multimap<String, UrlMapping> urlMappings = ArrayListMultimap.create();

    private final RequestMappingHandlerMapping mapping;


    public DefaultUrlMappingProvider(RequestMappingHandlerMapping mapping) {
        this.mapping = mapping;
    }

    /**
     * 匹配Mapping信息
     * @param request 请求信息
     * @return 匹配的结果，如果没有匹配上则返回null
     */
    @Nullable
    @Override
    public UrlMapping matchMapping(@NonNull HttpServletRequest request) {
        String currentMethod = request.getMethod();
        UrlMapping urlMapping = null;

        for (String uri : this.urlMappings.keySet()) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(uri);
            if (antPathMatcher.matches(request)) {
                Collection<UrlMapping> urlMappingList = this.urlMappings.get(uri);
                // 获取对应的请求
                List<UrlMapping> filterUrlMapping = urlMappingList.stream()
                        .filter(item -> {
                            if (item.getRequestMethod() == null) {
                                return true;
                            }
                            return StringUtils.equals(currentMethod, item.getRequestMethod().name());
                        }).collect(Collectors.toList());
                if (filterUrlMapping.isEmpty()) {
                    throw new AccessDeniedException(I18nUtils.get(HttpStatus.METHOD_NOT_ALLOWED));
                }
                // 判断方法是否需要校验
                urlMapping = filterUrlMapping.get(0);
                break;
            }
        }
        return urlMapping;
    }

    @Override
    public Multimap<String, UrlMapping> getAllMapping() {
        return this.urlMappings;
    }


    @Override
    public void afterPropertiesSet() {
        this.initAllMapping();
    }

    /**
     * 初始化映射信息
     */
    protected void initAllMapping() {
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
            PatternsRequestCondition condition = requestMappingInfo.getPatternsCondition();
            if (condition != null) {
                // 获取当前 key 下的获取所有URL
                Set<String> urls = condition.getPatterns();
                RequestMethodsRequestCondition method = requestMappingInfo.getMethodsCondition();
                urls.forEach(s -> {
                    if (method.getMethods().isEmpty()) {
                        UrlMapping urlMapping = new UrlMapping();
                        urlMapping.setRequestMethod(null);
                        urlMapping.setHandlerMethod(handlerMethod);
                        urlMappings.put(s, urlMapping);
                    } else {
                        List<UrlMapping> urlMappingList = method.getMethods().stream()
                                .map(requestMethod -> {
                                    UrlMapping urlMapping = new UrlMapping();
                                    urlMapping.setRequestMethod(requestMethod);
                                    urlMapping.setHandlerMethod(handlerMethod);
                                    return urlMapping;
                                }).collect(Collectors.toList());
                        urlMappings.putAll(s, urlMappingList);
                    }
                });
            }
        });
    }

}
