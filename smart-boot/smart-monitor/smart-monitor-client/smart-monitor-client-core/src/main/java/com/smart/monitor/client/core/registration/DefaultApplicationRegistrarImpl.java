package com.smart.monitor.client.core.registration;

import com.smart.monitor.client.core.application.ApplicationFactory;
import com.smart.monitor.core.model.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 默认的注册类
 * @author shizhongming
 * 2021/3/21 8:33 上午
 */
@Slf4j
public class DefaultApplicationRegistrarImpl implements ApplicationRegistrar {


    private final ApplicationFactory applicationFactory;

    private final List<String> serverUrls;

    private final boolean once;

    private final RegistrarClient registrarClient;

    private final AtomicReference<String> registeredId =new AtomicReference<String>();

    public DefaultApplicationRegistrarImpl(@NonNull ApplicationFactory applicationFactory, @NonNull List<String> serverUrls, boolean once, @NonNull RegistrarClient registrarClient) {
        this.applicationFactory = applicationFactory;
        this.serverUrls = serverUrls;
        this.once = once;
        this.registrarClient = registrarClient;
    }

    @Override
    public boolean register() {
        final Application application = this.applicationFactory.createApplication();
        boolean registerSuccess = false;
        for (String url : this.serverUrls) {
            final boolean success = this.register(url, application);
            if (success) {
                registerSuccess = true;
                if (this.once) {
                    break;
                }
            }
        }
        return registerSuccess;
    }

    /**
     * 执行注册
     * @param url 服务端URL
     * @param application 客户端
     * @return 注册结果
     */
    protected boolean register(String url, Application application) {
        try {
            log.debug("application start register, register url: {}, application code: {}", url, application.getApplicationCode());
            final String id = this.registrarClient.register(url, application);
            if (this.registeredId.compareAndSet(null, id)) {
                log.info("application registered success, server url: {}, id: {}", url, id);
            } else {
                log.info("application refreshed success, server url: {}, id: {}", url, id);
            }
            return true;
        } catch (Exception e) {
            log.warn("application registered failed, server url: {}, error message: {}", url, e.getMessage());
            return false;
        }
    }

    @Override
    public void deregister() {
        final String id = this.registeredId.get();
        if (!StringUtils.hasLength(id)) {
            return;
        }
        for (String url : this.serverUrls) {
            try {
                log.debug("application start deregister, server url: {}, id: {}", url, id);
                this.registrarClient.deregister(url, id);
                log.info("application deregister success, server url: {}, id: {}", url, id);
                this.registeredId.compareAndSet(id, null);
                if (this.once) {
                    break;
                }
            } catch (Exception e) {
                log.warn("application deregister failed, client id: {}, server url: {}, error message: {}", id, url, e.getMessage());
            }
        }
    }

    @Override
    @Nullable
    public String getRegisteredId() {
        return this.registeredId.get();
    }
}
