package com.smart.framework.auth.extensions.wechat.provider;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.extensions.wechat.model.WechatAppLoginResult;
import com.smart.framework.auth.extensions.wechat.model.WechatLoginResult;
import com.smart.framework.commons.core.i18n.I18nUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * @author zhongming4762
 * 2023/4/3
 */
@Slf4j
public class WechatMiniappLoginProvider implements WechatLoginProvider {

    private final WxMaService wxMaService;


    public WechatMiniappLoginProvider(WxMaService wxMaService) {
        this.wxMaService = wxMaService;
    }

    @SneakyThrows(WxErrorException.class)
    @Override
    public WechatLoginResult login(String appid, Object credentials) {
        String code = (String) credentials;
        if (!this.wxMaService.switchover(appid)) {
            throw new AuthenticationServiceException(I18nUtils.get(AuthI18nMessage.WECHAT_APP_CONFIG_NOT_FOUND_APPID, (Object) appid));
        }
        WxMaJscode2SessionResult sessionInfo = this.wxMaService.getUserService().getSessionInfo(code);
        log.info("微信请求登录成功，openid：【{}】，session key：【{}】", sessionInfo.getOpenid(), sessionInfo.getSessionKey());
        WechatAppLoginResult result = new WechatAppLoginResult();
        result.setOpenid(sessionInfo.getOpenid());
        result.setSessionKey(sessionInfo.getSessionKey());
        result.setUnionid(sessionInfo.getUnionid());
        return result;
    }

    @Override
    public AuthTypeEnum supportLoginType() {
        return AuthTypeEnum.WECHAT_MINIAPP;
    }
}
