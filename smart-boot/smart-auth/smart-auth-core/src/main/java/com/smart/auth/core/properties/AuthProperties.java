package com.smart.auth.core.properties;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.List;

/**
 * auth配置参数
 * @author jackson
 * 2020/1/23 9:25 上午
 */
@ConfigurationProperties(prefix = "gc.auth")
@Getter
@Setter
public class AuthProperties {

    private String jwtKey = "smart-boot-auth";

    private String prefix = "smart:session";

    private boolean jwt = false;

    private Session session = new Session();

    private Saml2 saml2 = new Saml2();

    private IgnoreConfig ignores = new IgnoreConfig();

    /**
     * 临时令牌配置
     */
    private TempToken tempToken = new TempToken();

    /**
     * 是否是开发模式
     */
    @NonNull
    private Boolean development = Boolean.FALSE;

    /**
     * 用户状态配置
     */
    private UserStatus status = new UserStatus();

    /**
     * appsecret 配置
     */
    private AppsecretProperties appsecret = new AppsecretProperties();

    /**
     * 临时令牌配置
     */
    @Getter
    @Setter
    public static class TempToken {
        /**
         * 超时时间，默认60S
         */
        private Duration timeout = Duration.ofSeconds(60);
    }

    @Getter
    @Setter
    public static class UserStatus {
        /**
         * 登录失败锁定次数
         */
        private Integer loginFailLockTime = 5;
    }

    @Getter
    @Setter
    public static class Session {
        private Timeout timeout = new Timeout();
    }

    @Getter
    @Setter
    public static class Timeout {
        // 默认 30分钟
        private Duration global = Duration.ofSeconds(1800L);
        // 默认30天
        private Duration mobile = Duration.ofSeconds(2592000L);
        // 默认7天
        private Duration remember = Duration.ofSeconds(604800L);
    }

    /**
     * APP SECRET配置
     */
    @Getter
    @Setter
    public static class AppsecretProperties {
        /**
         * access token 过期时间 7200秒
         */
        private Duration accessTokenTimeout = Duration.ofSeconds(7200);

        /**
         * 旧 Access token过期时间
         */
        private Duration oldAccessTokenTimeout = Duration.ofSeconds(5L * 60L);

        /**
         * 随机串的过期时间 默认15分钟，随机串使用后指定时间内不可在用
         */
        private Duration noncestrTimeout = Duration.ofSeconds(15L * 60L);

        /**
         * 随机串的过期时间 默认5分钟
         */
        private Duration timestampTimeout = Duration.ofSeconds(5L * 60L);
    }


    /**
     * 忽略的请求设置
     */
    @Getter
    @Setter
    public static class IgnoreConfig {

        /**
         * 需要忽略的 URL 格式，不考虑请求方法
         */
        private List<String> pattern = Lists.newArrayList("/public/**");

        public List<String> getPattern() {
            String publicStr = "/public/**";
            if (!pattern.contains(publicStr)) {
                pattern.add(publicStr);
            }
            return pattern;
        }

        /**
         * 需要忽略的 GET 请求
         */
        private List<String> get = Lists.newArrayList();

        /**
         * 需要忽略的 POST 请求
         */
        private List<String> post = Lists.newArrayList();

        /**
         * 需要忽略的 DELETE 请求
         */
        private List<String> delete = Lists.newArrayList();

        /**
         * 需要忽略的 PUT 请求
         */
        private List<String> put = Lists.newArrayList();

        /**
         * 需要忽略的 HEAD 请求
         */
        private List<String> head = Lists.newArrayList();

        /**
         * 需要忽略的 PATCH 请求
         */
        private List<String> patch = Lists.newArrayList();

        /**
         * 需要忽略的 OPTIONS 请求
         */
        private List<String> options = Lists.newArrayList();

        /**
         * 需要忽略的 TRACE 请求
         */
        private List<String> trace = Lists.newArrayList();
    }

    /**
     * SAML配置
     */
    @Getter
    @Setter
    public static class Saml2 {

        private String entityId;

        private KeyStore key = new KeyStore();

        private Identity identity = new Identity();

        /**
         * entityBaseURL
         */
        private String entityBaseUrl;

        /**
         * 重试次数
         */
        private Integer retry = 5;

        /**
         * key 配置
         */
        @Getter
        @Setter
        public static class KeyStore {
            private String name;

            private String password;

            private String filePath;
        }

        @Getter
        @Setter
        public static class Identity {
            private Boolean discoveryEnabled = Boolean.TRUE;

            private String metadataFilePath;
        }
    }
}
