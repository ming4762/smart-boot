package com.smart.framework.extension.captcha.resource;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.CrudResourceStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 验证码资源加载器
 * @author shizhongming
 * 2025/2/17 9:39
 * @since 5.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class CaptchaResourceLoader implements InitializingBean {

    private static final String CLASSPATH_PREFIX = "classpath";
    private static final String RESOURCE_PREFIX = "/**/*.{jpg,png}";

    private final CrudResourceStore resourceStore;

    private final List<String> resourceList;

    @Override
    public void afterPropertiesSet() {
        this.loadResource();
    }

    public void loadResource() {
        if (CollectionUtils.isEmpty(resourceList)) {
            return;
        }
        this.resourceList.stream()
                .flatMap(resource -> {
                    String path = resource + RESOURCE_PREFIX;
                    if (path.startsWith(CLASSPATH_PREFIX)) {
                        return this.getClasspathResource(path).stream();
                    }
                    throw new IllegalArgumentException("不支持的资源类型");
                }).forEach(item -> resourceStore.addResource(CaptchaTypeConstant.SLIDER, item));
    }

    private List<cloud.tianai.captcha.resource.common.model.dto.Resource> getClasspathResource(String resourcePath) {
        return Arrays.stream(this.getResourceByPath(resourcePath))
                .map(item -> {
                    ClassPathResource classPathResource = (ClassPathResource) item;
                    String path = classPathResource.getPath();
                    return new cloud.tianai.captcha.resource.common.model.dto.Resource(CLASSPATH_PREFIX, path, null);
                }).toList();
    }

    private Resource[] getResourceByPath(String path) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            return resolver.getResources(path);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            return new Resource[0];
        }
    }
}
