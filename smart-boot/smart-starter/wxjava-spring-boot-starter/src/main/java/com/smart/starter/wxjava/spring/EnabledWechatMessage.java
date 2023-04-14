package com.smart.starter.wxjava.spring;

import com.smart.starter.wxjava.spring.config.WechatMessageConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用微信消息
 * @author zhongming4762
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(WechatMessageConfig.class)
public @interface EnabledWechatMessage {
}
