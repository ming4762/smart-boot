package com.smart.cloud.starter.feign.interceptor;

import com.smart.framework.commons.core.constants.HttpHeaderConstants;
import com.smart.framework.commons.core.utils.SmartCloudUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * 将当前服务名添加到请求头中，后面用于服务间调用时识别
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-11 15:37
 * @since 5.0.0
 */
public class FeignServiceTraceInterceptor implements RequestInterceptor {

    private static final String HEADER_SPLIT = ",";

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void apply(RequestTemplate template) {
        List<String> serviceList = SmartCloudUtils.getServiceTrace();
        List<String> headerListWithThis = new ArrayList<>(serviceList.size() + 1);
        headerListWithThis.addAll(serviceList);
        headerListWithThis.add(appName);
        template.header(HttpHeaderConstants.SERVICE_TRACE, String.join(HEADER_SPLIT, headerListWithThis));
    }

}
