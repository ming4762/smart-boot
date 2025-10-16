package com.smart.framework.extension.wechat.message;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.extension.wechat.constants.WechatEventEnum;
import com.smart.framework.extension.wechat.constants.WechatMsgTypeEnum;
import com.smart.framework.extension.wechat.message.dto.WechatMessageCheckDTO;
import com.smart.framework.extension.wechat.message.dto.WechatMessageResultDTO;
import com.smart.framework.extension.wechat.message.event.WechatMessageEvent;
import com.smart.framework.extension.wechat.message.event.WechatSubscribeEvent;
import com.smart.framework.extension.wechat.message.event.WechatUnSubscribeEvent;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.XmlUtils;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

/**
 * 微信消息controller
 * @author zhongming4762
 * 2023/4/7
 */
@RequestMapping("public/wechat/message")
@Slf4j
@RestController
public class WechatMessageController {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final WxMpService wxMpService;

    public WechatMessageController(ApplicationEventPublisher applicationEventPublisher, WxMpService wxMpService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.wxMpService = wxMpService;
    }


    /**
     * 微信消息验证接口
     * @param parameter 参数
     * @return 是否成功
     */
    @GetMapping
    public String check(WechatMessageCheckDTO parameter) {
        if (this.doCheck(parameter)) {
            return parameter.getEchostr();
        }
        return null;
    }

    private boolean doCheck(WechatMessageCheckDTO parameter) {
        return this.wxMpService.checkSignature(parameter.getTimestamp(), parameter.getNonce(), parameter.getSignature());
    }

    /**
     * 消息接收接口
     * @return 结果
     */
    @PostMapping
    public Object message(@RequestBody String message, WechatMessageCheckDTO parameter) {
        if (!this.doCheck(parameter)) {
            return false;
        }
        log.info("wechat message：{}", message);
        WechatMessageResultDTO result = JsonUtils.parse(JsonUtils.toJsonString(XmlUtils.xml2Map(message)), WechatMessageResultDTO.class);
        if (WechatMsgTypeEnum.TEXT.equals(result.getMsgType())) {
            // 发送消息事件
            WechatMessageEvent messageEvent = new WechatMessageEvent(this);
            BeanUtils.copyProperties(result, messageEvent);
            this.applicationEventPublisher.publishEvent(messageEvent);
            return "";
        }
        if (WechatMsgTypeEnum.EVENT.equals(result.getMsgType())) {
            // 事件
            if (WechatEventEnum.SUBSCRIBE.equals(result.getEvent())) {
                WechatSubscribeEvent subscribeEvent = new WechatSubscribeEvent(this);
                BeanUtils.copyProperties(result, subscribeEvent);
                this.applicationEventPublisher.publishEvent(subscribeEvent);
                return "";
            }
            if (WechatEventEnum.UNSUBSCRIBE.equals(result.getEvent())) {
                WechatUnSubscribeEvent unSubscribeEvent = new WechatUnSubscribeEvent(this);
                BeanUtils.copyProperties(result, unSubscribeEvent);
                this.applicationEventPublisher.publishEvent(unSubscribeEvent);
                return "";
            }
        }
        return false;
    }
}
