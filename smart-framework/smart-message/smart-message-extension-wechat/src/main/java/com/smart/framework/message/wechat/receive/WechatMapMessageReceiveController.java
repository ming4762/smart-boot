package com.smart.framework.message.wechat.receive;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.extension.wechat.constants.WechatEventEnum;
import com.smart.framework.extension.wechat.constants.WechatMsgTypeEnum;
import com.smart.framework.extension.wechat.event.message.WechatMessageCommonEvent;
import com.smart.framework.extension.wechat.event.message.WechatMessageSubscribeEvent;
import com.smart.framework.extension.wechat.event.message.WechatMessageTextEvent;
import com.smart.framework.extension.wechat.event.message.WechatMessageUnSubscribeEvent;
import com.smart.framework.extension.wechat.pojo.dto.WechatMessageCheckDTO;
import com.smart.framework.extension.wechat.pojo.dto.WechatMessageResultDTO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.XmlUtils;
import me.chanjar.weixin.mp.api.WxMpService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.*;

/**
 * 微信消息controller
 * @author zhongming4762
 * 2023/4/7
 */
@RequestMapping("public/wechat/message/map/receive")
@Slf4j
@RestController
public class WechatMapMessageReceiveController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final WxMpService wxMpService;

    public WechatMapMessageReceiveController(WxMpService wxMpService) {
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
        log.info("wechat message check：{}", JsonUtils.toJsonString(parameter));
        boolean result = this.wxMpService.checkSignature(parameter.getTimestamp(), parameter.getNonce(), parameter.getSignature());
        if (result) {
            log.info("wechat message check success");
        } else {
            log.warn("wechat message check fail");
        }
        return result;
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
            WechatMessageTextEvent messageEvent = new WechatMessageTextEvent(this, result);
            this.applicationContext.publishEvent(messageEvent);
            return "";
        }
        if (WechatMsgTypeEnum.EVENT.equals(result.getMsgType())) {
            // 事件
            if (WechatEventEnum.SUBSCRIBE.equals(result.getEvent())) {
                WechatMessageSubscribeEvent subscribeEvent = new WechatMessageSubscribeEvent(this, result);
                this.applicationContext.publishEvent(subscribeEvent);
                return "";
            }
            if (WechatEventEnum.UNSUBSCRIBE.equals(result.getEvent())) {
                WechatMessageUnSubscribeEvent unSubscribeEvent = new WechatMessageUnSubscribeEvent(this, result);
                this.applicationContext.publishEvent(unSubscribeEvent);
                return "";
            }
            WechatMessageCommonEvent commonEvent = new WechatMessageCommonEvent(this, result);
            this.applicationContext.publishEvent(commonEvent);
            return "";
        }
        log.warn("wechat message type not support：{}", result.getMsgType());
        return "";
    }


    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
