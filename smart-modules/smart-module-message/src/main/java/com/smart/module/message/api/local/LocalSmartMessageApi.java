package com.smart.module.message.api.local;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.message.core.constants.SmartMessageChannelType1Enum;
import com.smart.framework.message.core.constants.SmartMessageChannelType2Enum;
import com.smart.framework.message.core.event.SmartMessageSendEvent;
import com.smart.framework.message.core.exception.SmartMessageException;
import com.smart.framework.message.core.pojo.dto.SmartMessageToUserDTO;
import com.smart.framework.message.core.service.SmartMessageSender;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.dto.SmsSendResult;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.message.model.SmartMessageChannelPO;
import com.smart.module.message.model.SmartMessageTemplatePO;
import com.smart.module.message.service.SmartMessageChannelService;
import com.smart.module.message.service.SmartMessageTemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 本地消息接口
 * @author zhongming4762
 * 2023/6/6
 */
@Component
@Primary
@Slf4j
public class LocalSmartMessageApi implements SmartMessageApi {

    private final ApplicationContext applicationContext;
    private final SmartMessageTemplateService smartMessageTemplateService;
    private final Configuration freemarkerConfiguration;
    private final SmartMessageChannelService smartMessageChannelService;
    private final SysUserApi sysUserApi;

    /**
     * 消息发送器
     */
    private final Map<String, SmartMessageSender> smartMessageSenderMap;

    public LocalSmartMessageApi(ApplicationContext applicationContext, SmartMessageTemplateService smartMessageTemplateService, Configuration freemarkerConfiguration, SmartMessageChannelService smartMessageChannelService, List<SmartMessageSender> smartMessageSenderList, SysUserApi sysUserApi) {
        this.applicationContext = applicationContext;
        this.smartMessageTemplateService = smartMessageTemplateService;
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.smartMessageChannelService = smartMessageChannelService;
        this.smartMessageSenderMap = smartMessageSenderList.stream()
                .collect(Collectors.toMap(
                        item -> Stream.of(item.supportChannel1(), item.supportChannel2())
                                .filter(Objects::nonNull)
                                .map(Enum::name)
                                .collect(Collectors.joining()),
                        item -> item
                ));
        this.sysUserApi = sysUserApi;
    }

    /**
     * 发送短信
     *
     * @param parameter 发送短信参数
     * @return 返回结果
     */
    @Override
    public SmsSendResult sendSms(RemoteSmsSendParameter parameter) {
        // TODO 待开发
        return null;
    }

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    public List<MessageSendResult> send(RemoteMessageSendParameter parameter) {
        if (CollectionUtils.isEmpty(parameter.getMessageChannelCodeList())) {
            throw new SmartMessageException("未指定发送通道");
        }
        // 查询消息通道
        Map<String, SmartMessageChannelPO> messageChannelMap = this.getValidateMessageChannel(new HashSet<>(parameter.getMessageChannelCodeList()));

        // 转换模板
        if (StringUtils.hasText(parameter.getTemplateCode()) && !StringUtils.hasText(parameter.getContent())) {
            parameter.setContent(this.getTemplateContent(parameter.getTemplateCode(), parameter.getTemplateData()));
        }
        // 查询用户信息
        List<SmartMessageToUserDTO> toUserList = CollectionUtils.isEmpty(parameter.getToUserIds()) ?
                List.of() :
                this.sysUserApi.listUserById(new ArrayList<>(parameter.getToUserIds())).stream()
                        .map(item -> {
                            SmartMessageToUserDTO dto = new SmartMessageToUserDTO();
                            BeanUtils.copyProperties(item, dto);
                            return dto;
                        }).toList();

        List<MessageSendResult> resultList = new ArrayList<>(messageChannelMap.size());
        messageChannelMap.values().forEach(channel -> {
            SmartMessageChannelType1Enum channelType1 = channel.getChannelType1();
            SmartMessageChannelType2Enum channelType2 = channel.getChannelType2();
            String channelSenderKey = Stream.of(channelType1, channelType2)
                    .filter(Objects::nonNull)
                    .map(Enum::name)
                    .collect(Collectors.joining());
            SmartMessageSender smartMessageSender = smartMessageSenderMap.get(channelSenderKey);
            if (smartMessageSender == null) {
                throw new SmartMessageException("不支持的通道类型：" + channelSenderKey);
            }
            MessageSendResult sendResult = smartMessageSender.send(channel.getChannelProperties(), toUserList, parameter);
            resultList.add(sendResult);
        });
        this.applicationContext.publishEvent(new SmartMessageSendEvent(parameter, resultList, this));
        return resultList;
    }

    @SneakyThrows({IOException.class, TemplateException.class})
    private String getTemplateContent(String templateCode, Object templateData) {
        List<SmartMessageTemplatePO> templateList = this.smartMessageTemplateService.list(
                new QueryWrapper<SmartMessageTemplatePO>().lambda()
                        .eq(SmartMessageTemplatePO::getTemplateCode, templateCode)
                        .eq(SmartMessageTemplatePO::getUseYn, Boolean.TRUE)
        );
        if (CollectionUtils.isEmpty(templateList)) {
            throw new SystemException("获取模版失败，模版编码：" + templateCode);
        }
        SmartMessageTemplatePO messageTemplate = templateList.getFirst();
        Template template = new Template(messageTemplate.getTemplateCode(), messageTemplate.getTemplateContent(), this.freemarkerConfiguration);
        StringWriter stringWriter = new StringWriter();
        template.process(templateData, stringWriter);
        return stringWriter.toString();
    }

    /**
     * 获取并验证通道信息
     * @param messageChannelCodes 通道编码
     * @return 消息通道
     */
    private Map<String, SmartMessageChannelPO> getValidateMessageChannel(Set<String> messageChannelCodes) {
        // 查询消息通道
        Map<String, SmartMessageChannelPO> channelCodeMap = this.smartMessageChannelService.lambdaQuery()
                .in(SmartMessageChannelPO::getChannelCode, messageChannelCodes)
                .list().stream()
                .collect(Collectors.toMap(SmartMessageChannelPO::getChannelCode, item -> item));
        String noHasCode = messageChannelCodes.stream()
                .filter(item -> !channelCodeMap.containsKey(item))
                .collect(Collectors.joining(","));
        if (StringUtils.hasText(noHasCode)) {
            throw new SmartMessageException(String.format("通道编码【%s】不存在", noHasCode));
        }
        String hasNoUseCode = messageChannelCodes.stream()
                .filter(item -> !Boolean.TRUE.equals(channelCodeMap.get(item).getUseYn()))
                .collect(Collectors.joining(","));
        if (StringUtils.hasText(hasNoUseCode)) {
            throw new SmartMessageException(String.format("通道编码【%s】已停用", hasNoUseCode));
        }
        return channelCodeMap;
    }
}
