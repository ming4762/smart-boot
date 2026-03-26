package com.smart.framework.auth.core.beans;

import com.smart.framework.commons.core.beans.AbstractBeanNameProvider;
import com.smart.framework.commons.core.http.HttpStatus;
import com.smart.framework.commons.core.i18n.I18nUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;

/**
 * 默认的映射提供器
 * @author ShiZhongMing
 * 2021/3/8 17:32
 * @since 1.0
 */
public class DefaultUrlMappingProvider extends AbstractBeanNameProvider implements InitializingBean, UrlMappingProvider {

    private final Map<String, List<UrlMapping>> urlMappings = new HashMap<>();

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

        for (Map.Entry<String, List<UrlMapping>> entry : this.urlMappings.entrySet()) {
            String uri = entry.getKey();
            List<UrlMapping> urlMappingList = entry.getValue();
            PathPatternRequestMatcher pathPatternMatcher = PathPatternRequestMatcher.pathPattern(uri);
            if (!pathPatternMatcher.matches(request)) {
                continue;
            }
            // 获取对应的请求
            List<UrlMapping> filterUrlMapping = urlMappingList.stream()
                    .filter(item -> {
                        if (item.getRequestMethod() == null) {
                            return true;
                        }
                        return currentMethod.equals(item.getRequestMethod().name());
                    }).toList();
            if (filterUrlMapping.isEmpty()) {
                throw new AccessDeniedException(I18nUtils.get(HttpStatus.METHOD_NOT_ALLOWED));
            }
            // 判断方法是否需要校验
            return filterUrlMapping.getFirst();
        }
        return null;
    }

    @Override
    public Map<String, List<UrlMapping>> getAllMapping() {
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
            PathPatternsRequestCondition pathPatternsCondition = requestMappingInfo.getPathPatternsCondition();
            if (pathPatternsCondition == null) {
                return;
            }
            Set<PathPattern> patterns = pathPatternsCondition.getPatterns();
            // 获取当前 key 下的获取所有URL
            RequestMethodsRequestCondition method = requestMappingInfo.getMethodsCondition();
            patterns.forEach(pathPattern -> {
                String s = pathPattern.getPatternString();
                if (method.getMethods().isEmpty()) {
                    UrlMapping urlMapping = new UrlMapping();
                    urlMapping.setRequestMethod(null);
                    urlMapping.setHandlerMethod(handlerMethod);
                    this.addMapping(s, urlMapping);
                } else {
                    List<UrlMapping> urlMappingList = method.getMethods().stream()
                            .map(requestMethod -> {
                                UrlMapping urlMapping = new UrlMapping();
                                urlMapping.setRequestMethod(requestMethod);
                                urlMapping.setHandlerMethod(handlerMethod);
                                return urlMapping;
                            }).toList();
                    this.addMapping(s, urlMappingList);
                }
            });
        });
    }

    protected void addMapping(String url, UrlMapping urlMapping) {
        this.urlMappings.computeIfAbsent(url, key -> new ArrayList<>()).add(urlMapping);
    }

    protected void addMapping(String url, List<UrlMapping> urlMappingList) {
        this.urlMappings.computeIfAbsent(url, key -> new ArrayList<>()).addAll(urlMappingList);
    }

}
