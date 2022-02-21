package com.smart.monitor.autoconfigure.server;

import com.smart.monitor.server.common.MonitorServerProperties;
import com.smart.monitor.server.core.SmartMonitorServer;
import com.smart.monitor.server.core.notify.mail.MailEventNotifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;

/**
 * 邮件通知自动配置类
 * @author ShiZhongMing
 * 2022/2/21
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "smart.monitor.server.notify.mail.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(SmartMonitorServer.class)
@ConditionalOnBean({JavaMailSender.class, SpringTemplateEngine.class})
@AutoConfigureAfter({
        MonitorServerAutoConfiguration.class,
        MailSenderAutoConfiguration.class,
        ThymeleafAutoConfiguration.class
})
public class MonitorServerMailNotifyAutoConfiguration {

    /**
     * 创建邮件事件通知类
     * @param properties 参数
     * @return MailEventNotifier
     */
    @Bean
    @ConditionalOnMissingBean
    public MailEventNotifier mailEventNotifier(MonitorServerProperties properties) {
        return new MailEventNotifier(properties.getNotify().getMail());
    }

    @Bean
    public ITemplateResolver mailClasspathTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setOrder(100);
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCacheable(false);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        return resolver;
    }
}
