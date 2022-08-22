package com.smart.monitor.client.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端配置
 * @author shizhongming
 * 2021/3/21
 */
@ConfigurationProperties("smart.monitor.client")
@Getter
@Setter
public class ClientProperties implements InitializingBean {

    private Instance instance = new Instance();

    private Registration registration = new Registration();

    private Auth auth = new Auth();

    private HttpTrace httpTrace = new HttpTrace();

    /**
     * 认证信息
     */
    @Getter
    @Setter
    public static class Auth {
        private String token;
        private Boolean enabled = Boolean.TRUE;
    }

    /**
     * HttpTrace配置信息
     */
    @Getter
    @Setter
    public static class HttpTrace {

        /**
         * 排除的URL列表
         */
        private String excludeUrls;
    }

    /**
     * 客户端信息
     */
    @Getter
    @Setter
    public static class Instance {

        /**
         * 客户端名称
         */
        @NonNull
        private String name;

        /**
         * 客户端的地址
         */
        @Nullable
        private String applicationUrl;

        /**
         * 元数据
         */
        @NonNull
        private Map<String, String> metadata = new HashMap<>();

    }

    /**
     * 注册配置参数
     */
    @Getter
    @Setter
    public static class Registration {

        @NonNull
        private String url;

        @NonNull
        private Boolean once = Boolean.TRUE;

        @NonNull
        private Duration interval = Duration.ofMillis(30000L);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.hasLength(this.instance.name, "application name is null, please init in config file, config key: smart.monitor.client.instance.name");
        Assert.hasLength(this.registration.url, "server url is null, please init in config file, config key: smart.monitor.client.registration.url");
    }
}
