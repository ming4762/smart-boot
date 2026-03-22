package com.smart.framework.message.wechat.sender;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.validate.utils.ValidatorUtils;
import com.smart.framework.extension.wechat.config.SmartWechatConfigStorageCreator;
import com.smart.framework.message.core.constants.SmartMessageChannelType1Enum;
import com.smart.framework.message.core.constants.SmartMessageChannelType2Enum;
import com.smart.framework.message.core.pojo.dto.SmartMessageToUserDTO;
import com.smart.framework.message.core.service.SmartMessageSender;
import com.smart.framework.message.wechat.properties.SmartWechatMpChannelProperties;
import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.dto.WechatMessageSendResult;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteWechatMpTemplateParameter;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.constants.SysThirdPlatformSubTypeEnum;
import com.smart.module.api.system.constants.SysThirdPlatformTypeEnum;
import com.smart.module.api.system.dto.SysUserThirdAccountDTO;
import com.smart.module.api.system.parameter.SysUserThirdAccountParameter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 微信服务号模板消息发送器
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-03 13:52
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class WechatMpTemplateMessageSender implements SmartMessageSender {

    private final WxMpService wxMpService;
    private final SmartWechatConfigStorageCreator smartWechatConfigStorageCreator;
    private final SysUserApi sysUserApi;


    /**
     * 获取支持的一级通道信息
     *
     * @return 支持的消息通道
     */
    @Override
    public @NonNull SmartMessageChannelType1Enum supportChannel1() {
        return SmartMessageChannelType1Enum.WECHAT;
    }

    /**
     * 获取支持的二级通道信息
     *
     * @return 支持的消息通道
     */
    @Override
    public @Nullable SmartMessageChannelType2Enum supportChannel2() {
        return SmartMessageChannelType2Enum.WECHAT_MP_TEMPLATE;
    }

    /**
     * 发送消息
     *
     * @param channelProperties 通道参数
     * @param toUserList        用户列表
     * @param parameter         消息发送参数
     * @return 消息发送结果
     */
    @SneakyThrows(WxErrorException.class)
    @Override
    public MessageSendResult send(@Nullable String channelProperties, List<SmartMessageToUserDTO> toUserList, RemoteMessageSendParameter parameter) {
        // 校验参数
        RemoteWechatMpTemplateParameter wechatMpTemplateParameter = parameter.getWechatMpTemplateParameter();
        if (wechatMpTemplateParameter == null) {
            throw new IllegalArgumentException("微信服务号模板消息发送参数不能为空");
        }
        SmartWechatMpChannelProperties mpChannelProperties = JsonUtils.parse(channelProperties, SmartWechatMpChannelProperties.class);
        // 校验参数
        ValidatorUtils.validate(wechatMpTemplateParameter);
        // 校验发送人
        String openid = wechatMpTemplateParameter.getOpenid();
        // 根据用户ID获取openid
        if (!StringUtils.hasText(openid)) {
            if (CollectionUtils.isEmpty(toUserList)) {
                throw new IllegalArgumentException("openid未指定时，toUserIds不能为空");
            }
            if (toUserList.size() > 1) {
                throw new IllegalArgumentException("微信服务号模板消息只能指定一个接收人");
            }
            Long userId = toUserList.getFirst().getUserId();
            // 查询用户的第三方账号
            List<SysUserThirdAccountDTO> accountList = this.sysUserApi.listUserThirdAccount(
                    SysUserThirdAccountParameter.builder()
                            .userIdList(List.of(userId))
                            .platformType(SysThirdPlatformTypeEnum.WECHAT)
                            .platformSubType(SysThirdPlatformSubTypeEnum.WECHAT_MP)
                            .appid(mpChannelProperties.getAppid())
                            .build()
            );
            if (CollectionUtils.isEmpty(accountList)) {
                throw new IllegalArgumentException("用户未绑定微信服务号账号,userId=" + userId);
            }
            openid = accountList.getFirst().getOpenid();
        }

        // 切换到指定的appid，如果没有则创建
        this.wxMpService.switchover(mpChannelProperties.getAppid(), _ -> this.smartWechatConfigStorageCreator.createMpConfigStorage(mpChannelProperties));

        WxMpTemplateMessage.WxMpTemplateMessageBuilder builder = WxMpTemplateMessage.builder()
                .toUser(openid)
                .templateId(wechatMpTemplateParameter.getTemplateId())
                .url(wechatMpTemplateParameter.getUrl())
                .clientMsgId(wechatMpTemplateParameter.getClientMsgId());
        // 设置小程序跳转信息
        if (wechatMpTemplateParameter.getMiniProgram() != null) {
            WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram();
            miniProgram.setAppid(wechatMpTemplateParameter.getMiniProgram().getAppid());
            miniProgram.setPagePath(wechatMpTemplateParameter.getMiniProgram().getPagePath());
            builder.miniProgram(miniProgram);
        }
        // 设置模板数据
        if (!CollectionUtils.isEmpty(wechatMpTemplateParameter.getData())) {
            builder.data(
                    wechatMpTemplateParameter.getData().stream()
                            .map(item -> new WxMpTemplateData(item.getName(), item.getValue(), item.getColor()))
                            .toList()
            );
        }
        String messageId = this.wxMpService.getTemplateMsgService().sendTemplateMsg(builder.build());
        WechatMessageSendResult result = new WechatMessageSendResult();
        result.setMessageId(messageId);
        return result;
    }
}
