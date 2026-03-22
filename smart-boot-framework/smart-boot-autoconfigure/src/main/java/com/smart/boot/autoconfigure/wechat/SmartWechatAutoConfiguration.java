package com.smart.boot.autoconfigure.wechat;

import com.smart.boot.autoconfigure.guava.GuavaCacheAutoConfiguration;
import com.smart.boot.autoconfigure.redis.SmartRedisAutoConfiguration;
import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.extension.wechat.config.DefaultSmartWechatConfigStorageCreatorImpl;
import com.smart.framework.extension.wechat.config.SmartWechatConfigStorageCreator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 微信自动配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 16:07
 * @since 5.0.0
 */
@EnableConfigurationProperties(SmartWechatProperties.class)
@Import({SmartWechatMpAutoconfiguration.class, SmartWechatMpAutoconfiguration.class})
@AutoConfigureAfter({ SmartRedisAutoConfiguration.class, GuavaCacheAutoConfiguration.class })
@ConditionalOnClass(SmartWechatConfigStorageCreator.class)
public class SmartWechatAutoConfiguration {

    /**
     * 默认的微信公众号配置存储创建器
     * @param properties 微信公众号配置属性
     * @param cacheServiceObjectProvider 缓存服务对象提供器
     * @return 配置存储创建器
     */
    @Bean
    @ConditionalOnMissingBean
    public SmartWechatConfigStorageCreator defaultSmartWechatConfigStorageCreator(
            SmartWechatProperties properties,
            ObjectProvider<CacheService> cacheServiceObjectProvider
    ) {
        return new DefaultSmartWechatConfigStorageCreatorImpl(properties.getKeyPrefix(), cacheServiceObjectProvider);
    }
}
