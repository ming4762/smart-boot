package com.smart.boot.rocketmq;

import com.smart.framework.rocketmq.event.SmartSpringEventConsumer;
import com.smart.framework.rocketmq.event.SmartSpringEventProducer;
import com.smart.framework.rocketmq.producer.SmartMqProducer;
import com.smart.framework.rocketmq.producer.SmartMqProducerRocketImpl;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ RocketMQ 自动配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 12:09
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SmartMqProperties.class)
@ConditionalOnClass(SmartMqProducer.class)
@ConditionalOnBean(RocketMQTemplate.class)
@AutoConfigureAfter(RocketMQAutoConfiguration.class)
public class SmartMqAutoconfiguration {

    @Bean
    @ConditionalOnBean(RocketMQTemplate.class)
    public SmartMqProducer smartMqProducer(SmartMqProperties properties, RocketMQTemplate rocketMQTemplate) {
        return new SmartMqProducerRocketImpl(properties.getPrefix(), rocketMQTemplate);
    }

    @Bean
    public SmartSpringEventProducer smartSpringEventProducer(SmartMqProperties properties, SmartMqProducer smartMqProducer) {
        return new SmartSpringEventProducer(String.join(":", properties.getEventTopic(), properties.getEventTag()), smartMqProducer);
    }

    @Bean
    public SmartSpringEventConsumer smartSpringEventConsumer() {
        return new SmartSpringEventConsumer();
    }
}
