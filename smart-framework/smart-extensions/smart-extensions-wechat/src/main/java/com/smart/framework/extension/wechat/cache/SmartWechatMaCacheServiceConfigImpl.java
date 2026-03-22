package com.smart.framework.extension.wechat.cache;

import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.smart.framework.commons.core.cache.CacheService;
import me.chanjar.weixin.common.bean.WxAccessToken;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

/**
 * 微信小程序缓存配置,基于smart-boot CacheService实现
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 16:48
 * @since 5.0.0
 */
public class SmartWechatMaCacheServiceConfigImpl extends WxMaDefaultConfigImpl {

    private static final String ACCESS_TOKEN_KEY_TPL = "%s:wxma:access_token:%s";
    private static final String MA_JSAPI_TICKET_KEY = "%swechat_ma_jsapi_ticket_key:%s";
    private static final String LOCK_KEY = "%s:wechat_ma_lock:%s";
    private static final String MA_CARD_API_TICKET_KEY = "%s:wechat_ma_card_api_ticket_key:%s";

    private final CacheService cacheService;
    private final String prefix;

    protected String accessTokenKey;
    protected String jsapiTicketKey;
    protected String cardApiTicketKey;
    protected String lockKey;

    public SmartWechatMaCacheServiceConfigImpl(CacheService cacheService, String prefix) {
        this.cacheService = cacheService;
        this.prefix = prefix;
    }

    @Override
    public void setAppid(String appId) {
        super.setAppid(appId);
        this.accessTokenKey = String.format(ACCESS_TOKEN_KEY_TPL, prefix, appId);
        this.jsapiTicketKey = String.format(MA_JSAPI_TICKET_KEY, prefix, appId);
        this.lockKey = String.format(LOCK_KEY, prefix, appId);
        this.cardApiTicketKey = String.format(MA_CARD_API_TICKET_KEY, prefix, appId);
    }

    @Override
    public Lock getAccessTokenLock() {
        return getLockByKey(this.lockKey.concat(":").concat("accessToken"));
    }

    @Override
    public Lock getCardApiTicketLock() {
        return getLockByKey(this.lockKey.concat(":").concat("cardApiTicket"));
    }

    @Override
    public Lock getJsapiTicketLock() {
        return getLockByKey(this.lockKey.concat(":").concat("jsapiTicket"));
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
    public void updateAccessToken(WxAccessToken accessToken) {
        updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        this.cacheService.put(this.accessTokenKey, accessToken, Duration.ofSeconds(expiresInSeconds));
    }

    @Override
    public String getJsapiTicket() {
        return this.cacheService.get(this.jsapiTicketKey);
    }

    @Override
    public boolean isJsapiTicketExpired() {
        Duration duration = this.cacheService.getExpire(this.jsapiTicketKey);
        if (duration == null) {
            return false;
        }
        return duration.compareTo(Duration.ofSeconds(5)) < 0;
    }

    @Override
    public void expireJsapiTicket() {
        this.cacheService.expire(this.jsapiTicketKey, Duration.ZERO);
    }

    @Override
    public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        this.cacheService.put(this.jsapiTicketKey, jsapiTicket, Duration.ofSeconds(expiresInSeconds - 200));
    }

    @Override
    public String getCardApiTicket() {
        return this.cacheService.get(this.cardApiTicketKey);
    }

    @Override
    public boolean isCardApiTicketExpired() {
        Duration duration = this.cacheService.getExpire(this.cardApiTicketKey);
        if (duration == null) {
            return false;
        }
        return duration.compareTo(Duration.ofSeconds(5)) < 0;
    }

    @Override
    public void expireCardApiTicket() {
        this.cacheService.expire(this.cardApiTicketKey, Duration.ZERO);
    }

    @Override
    public void updateCardApiTicket(String cardApiTicket, int expiresInSeconds) {
        this.cacheService.put(this.cardApiTicketKey, cardApiTicket, Duration.ofSeconds(expiresInSeconds));
    }

    @Override
    public void expireAccessToken() {
        this.cacheService.expire(this.accessTokenKey, Duration.ZERO);
    }

    @Override
    public long getExpiresTime() {
        return Optional.ofNullable(this.cacheService.getExpire(this.accessTokenKey))
                .map(Duration::toSeconds)
                .orElse(-1L);
    }

    protected Lock getLockByKey(String key) {
        return this.cacheService.getLock(key);
    }


}
