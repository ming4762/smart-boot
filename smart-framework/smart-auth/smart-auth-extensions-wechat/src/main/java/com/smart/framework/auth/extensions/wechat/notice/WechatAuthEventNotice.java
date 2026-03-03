package com.smart.framework.auth.extensions.wechat.notice;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.event.AuthEventHandler;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteWechatMpTemplateParameter;
import com.smart.module.api.system.SysParameterApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import java.util.Set;

/**
 * 微信认证事件通知
 * 微信登录成功后，发送微信消息
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 19:39
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class WechatAuthEventNotice implements AuthEventHandler {

    private final SmartMessageApi smartMessageApi;

    /**
     * 登录成功事件
     *
     * @param event 事件
     */
    @SneakyThrows(WxErrorException.class)
    @Override
    public void handleLoginSuccess(AuthenticationSuccessEvent event) {
        RestUserDetails user = (RestUserDetails) event.getAuthentication().getPrincipal();
        // 判断是否微信服务号扫码登录登录
        if (!AuthTypeEnum.WECHAT_MP_QRCODE.equals(user.getAuthType())) {
            return;
        }
        RemoteWechatMpTemplateParameter mpTemplateParameter = RemoteWechatMpTemplateParameter.builder()
                .build();
        RemoteMessageSendParameter parameter = RemoteMessageSendParameter.builder()
                .toUserIds(Set.of(user.getUserId()))
                .wechatMpTemplateParameter(mpTemplateParameter)
                .build();
        this.smartMessageApi.send(parameter);

    }
}
