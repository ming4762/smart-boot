package com.smart.framework.auth.core.share;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户springsecurity配置共享的需要校验登录验证码的URL
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-06 18:47
 * @since 5.0.0
 */
@Getter
@Setter
public class AuthSharedCaptchaLoginUrl implements Serializable {

    private Set<String> loginUrls;

    public AuthSharedCaptchaLoginUrl() {
        this.loginUrls = HashSet.newHashSet(10);
    }
}
