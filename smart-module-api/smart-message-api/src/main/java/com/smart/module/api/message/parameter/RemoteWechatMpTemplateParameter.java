package com.smart.module.api.message.parameter;

import com.smart.module.api.message.model.WechatMpTemplateData;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 微信公众号模板消息参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-03 16:13
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RemoteWechatMpTemplateParameter implements Serializable {

    /**
     * 用户的openid
     * 如果配置此参数，优先通过该参数发送
     * 如果不配置此参数，通过toUserIds查询openid
     */
    private String openid;

    @NotBlank(message = "模板ID不能为空")
    private String templateId;

    /**
     * 模板跳转链接（海外账号没有跳转能力,url 和 miniprogram 同时不填，无跳转，url 和 miniprogram 同时填写，优先跳转小程序
     */
    private String url;

    /**
     * 跳转小程序时填写（url 和 miniprogram 同时不填，无跳转，page 和 miniprogram 同时填写，优先跳转小程序）
     */
    private WechatMiniProgramParameter miniProgram;

    /**
     * 防重入id。对于同一个openid + client_msg_id, 只发送一条消息,10分钟有效,超过10分钟不保证效果。若无防重入需求，可不填
     */
    private String clientMsgId;

    private List<WechatMpTemplateData> data;
}
