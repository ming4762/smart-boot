package com.smart.framework.auth.core.properties;

import com.smart.framework.commons.core.dto.common.SmartFont;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;

/**
 * auth配置参数
 * @author jackson
 * 2020/1/23 9:25 上午
 */
@Getter
@Setter
@ConfigurationProperties("smart.auth")
public class AuthProperties implements InitializingBean {

    /**
     * 默认的验证码资源路径
     */
    private static final String DEFAULT_CAPTCHA_RESOURCE_PATH = "auth/captcha/*.jpg";

    /**
     * 认证缓存前缀
     */
    private String prefix = "smart-session";

    /**
     * 登录URL
     */
    private String loginUrl = "/auth/login";

    /**
     * JWT配置
     */
    private JwtProperties jwt = new JwtProperties();

    /**
     * session配置
     */
    private Session session = new Session();

    /**
     * SAML2配置
     */
    private AuthSaml2Properties saml2 = new AuthSaml2Properties();

    /**
     * 忽略权限验证配置
     */
    private AuthIgnoreProperties ignores = new AuthIgnoreProperties();

    /**
     * 临时令牌配置
     */
    private TempToken tempToken = new TempToken();

    /**
     * 是否是开发模式
     */
    private Boolean development;

    /**
     * 短信登录参数
     */
    private SmsProperties sms = new SmsProperties();

    /**
     * 验证码参数
     */
    private AuthCaptchaProperties captcha = new AuthCaptchaProperties();

    /**
     * access secret模式配置
     */
    private AuthAccessSecretProperties accessSecret = new AuthAccessSecretProperties();

    @Override
    public void afterPropertiesSet() {
        if (Boolean.TRUE.equals(this.jwt.enabled)) {
            Assert.notNull(this.jwt.privateKey, "JWT私钥路径不能为空");
            Assert.notNull(this.jwt.publicKey, "JWT公钥路径不能为空");
        }

    }

    @Getter
    @Setter
    public static class JwtProperties {
        private Boolean enabled = Boolean.FALSE;

        /**
         * 是否使用缓存存储权限信息
         */
        private Boolean permissionCache = Boolean.FALSE;

        private String privateKey = "classpath:auth/jwt/key/pri.key";

        private String publicKey = "classpath:auth/jwt/key/pub.key";
    }

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
     * 短信登录参数
     */
    @Getter
    @Setter
    public static class SmsProperties {

        /**
         * 短信签名
         */
        private String signName;

        /**
         * 短息模板
         */
        private String template;
    }

    /**
     * ACCESS SECRET 认证参数
     */
    @Getter
    @Setter
    public static class AuthAccessSecretProperties {
        /**
         * 匹配的URL
         */
        private List<String> urlMatcher;

        private String tokenPrefix = "SMART-BOOT";

        /**
         * 过期时间默认10分钟
         */
        private Duration expire = Duration.ofMinutes(10);
    }

    /**
     * 图片验证码参数
     */
    @Getter
    @Setter
    public static class ImageCaptchaProperties implements Serializable {

        @Serial
        private static final long serialVersionUID = -2278503531404987655L;
    }

    /**
     * 文本验证码参数
     */
    @Getter
    @Setter
    public static class TextCaptchaProperties implements Serializable {
        @Serial
        private static final long serialVersionUID = -6876970459792547301L;
        /**
         * 字符长度
         */
        private Integer length = 4;

        /**
         * 宽度
         */
        private Integer width = 130;

        /**
         * 高度
         */
        private Integer height = 48;

        /**
         * 字体
         */
        private SmartFont font;

        /**
         * 文本类型
         * 1 字母数字混合
         * 2 纯数字
         * 3 纯字母
         * 4 纯大写字母
         * 5 纯小写字母
         * 6 数字大写字母
         */
        private Integer charType;

        /**
         * 是否忽略大小写，默认忽略
         */
        private Boolean ignoreCase = Boolean.TRUE;

    }
}
