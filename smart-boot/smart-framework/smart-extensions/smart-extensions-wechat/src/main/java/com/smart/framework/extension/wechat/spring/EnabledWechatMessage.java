package com.smart.framework.extension.wechat.spring;

import com.smart.framework.extension.wechat.spring.config.WechatMessageConfig;
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
