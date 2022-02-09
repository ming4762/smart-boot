package com.smart.monitor.client.core.application;

import com.smart.monitor.client.core.properties.ClientProperties;
import com.smart.monitor.core.model.Application;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2021/3/21 8:24 上午
 */
public class DefaultApplicationFactoryImpl implements ApplicationFactory, ApplicationContextAware {

    private final ClientProperties clientProperties;

    private final ServerProperties serverProperties;

    private WebEndpointsSupplier webEndpointsSupplier;

    private ServletEndpointsSupplier servletEndpointsSupplier;

    private ControllerEndpointsSupplier controllerEndpointsSupplier;

    private ApplicationContext applicationContext;

    public DefaultApplicationFactoryImpl(ClientProperties clientProperties, ServerProperties serverProperties) {
        this.clientProperties = clientProperties;
        this.serverProperties = serverProperties;
    }

    @Override
    public Application createApplication() {
        // 获取客户端的地址
        String clientUrl = clientProperties.getInstance().getApplicationUrl() == null ? this.getClientUrl() : clientProperties.getInstance().getApplicationUrl();
        return Application.builder()
                .endPoints(this.getEndPointIds())
                .applicationName(this.clientProperties.getInstance().getName())
                .clientUrl(clientUrl)
                .startupTime(this.applicationContext.getStartupDate())
                .metadata(this.clientProperties.getInstance().getMetadata())
                .build();
    }

    /**
     * 获取客户端地址
     * @return 客户端地址
     */
    @SneakyThrows
    private String getClientUrl() {
        String contextPath = this.serverProperties.getServlet().getContextPath() == null ? "/" : this.serverProperties.getServlet().getContextPath();
        return String.format("http://%s:%s%s", InetAddress.getLocalHost().getHostAddress(), this.serverProperties.getPort(), contextPath);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取端点ID集合
     * @return 端点ID集合
     */
    protected Set<String> getEndPointIds() {
        final List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        if (this.webEndpointsSupplier != null) {
            allEndpoints.addAll(this.webEndpointsSupplier.getEndpoints());
        }
        if (this.servletEndpointsSupplier != null) {
            allEndpoints.addAll(this.servletEndpointsSupplier.getEndpoints());
        }
        if (this.controllerEndpointsSupplier != null) {
            allEndpoints.addAll(this.controllerEndpointsSupplier.getEndpoints());
        }
        return allEndpoints.stream()
                .map(item -> item.getEndpointId().toString())
                .collect(Collectors.toSet());
    }

    @Autowired(required = false)
    public void setWebEndpointsSupplier(WebEndpointsSupplier webEndpointsSupplier) {
        this.webEndpointsSupplier = webEndpointsSupplier;
    }

    @Autowired(required = false)
    public void setServletEndpointsSupplier(ServletEndpointsSupplier servletEndpointsSupplier) {
        this.servletEndpointsSupplier = servletEndpointsSupplier;
    }

    @Autowired(required = false)
    public void setControllerEndpointsSupplier(ControllerEndpointsSupplier controllerEndpointsSupplier) {
        this.controllerEndpointsSupplier = controllerEndpointsSupplier;
    }
}
