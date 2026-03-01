package com.smart.framework.extension.wechat.cache;

import com.smart.framework.commons.core.cache.CacheService;
import lombok.EqualsAndHashCode;
import me.chanjar.weixin.common.enums.TicketType;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

import java.time.Duration;

/**
 * 微信MP缓存配置,基于smart-boot CacheService实现
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 16:10
 * @since 5.0.0
 */
@EqualsAndHashCode(callSuper = true)
public class SmartWechatMpCacheServiceConfigImpl extends WxMpDefaultConfigImpl {

    private static final String ACCESS_TOKEN_KEY_TPL = "%s:wxmp:access_token:%s";
    private static final String TICKET_KEY_TPL = "%s:wxmp:ticket:key:%s:%s";

    private final transient CacheService cacheService;
    private final String prefix;

    private String accessTokenKey;

    public SmartWechatMpCacheServiceConfigImpl(CacheService cacheService, String prefix) {
        this.cacheService = cacheService;
        this.prefix = prefix;
    }

    @Override
    public void setAppId(String appId) {
        super.setAppId(appId);
        this.accessTokenKey = String.format(ACCESS_TOKEN_KEY_TPL, prefix, appId);
    }

    @Override
    public String getAccessToken() {
        return this.cacheService.get(this.accessTokenKey);
    }

    @Override
    public boolean isAccessTokenExpired() {
        Duration duration = this.cacheService.getExpire(this.accessTokenKey);
        if (duration == null) {
            return false;
        }
        return duration.compareTo(Duration.ofSeconds(5)) < 0;
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        this.cacheService.put(this.accessTokenKey, accessToken, Duration.ofSeconds(expiresInSeconds));
    }

    @Override
    public void expireAccessToken() {
        this.cacheService.expire(this.accessTokenKey, Duration.ZERO);
    }

    @Override
    public String getTicket(TicketType type) {
        return this.cacheService.get(this.getTicketKey(type));
    }

    @Override
    public boolean isTicketExpired(TicketType type) {
        Duration duration = this.cacheService.getExpire(this.getTicketKey(type));
        if (duration == null) {
            return false;
        }
        return duration.compareTo(Duration.ofSeconds(5)) < 0;
    }

    @Override
    public synchronized void updateTicket(TicketType type, String jsapiTicket, int expiresInSeconds) {
        this.cacheService.put(this.getTicketKey(type), jsapiTicket, Duration.ofSeconds(expiresInSeconds - 200));
    }

    @Override
    public void expireTicket(TicketType type) {
        this.cacheService.expire(this.getTicketKey(type), Duration.ZERO);
    }

    private String getTicketKey(TicketType type) {
        return String.format(TICKET_KEY_TPL, this.prefix, appId, type.getCode());
    }
}
