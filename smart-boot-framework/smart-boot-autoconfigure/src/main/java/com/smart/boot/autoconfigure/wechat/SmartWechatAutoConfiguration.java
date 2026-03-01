package com.smart.boot.autoconfigure.wechat;

import com.smart.boot.autoconfigure.guava.GuavaCacheAutoConfiguration;
import com.smart.boot.autoconfigure.redis.SmartRedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
public class SmartWechatAutoConfiguration {
}
