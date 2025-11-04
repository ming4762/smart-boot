package com.smart.framework.message.email.sender;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.message.core.constants.SmartMessageChannelType1Enum;
import com.smart.framework.message.core.exception.SmartMessageException;
import com.smart.framework.message.core.pojo.dto.SmartMessageToUserDTO;
import com.smart.framework.message.core.service.SmartMessageSender;
import com.smart.framework.message.email.SmartMessageEmailChannelProperties;
import com.smart.framework.message.email.dto.EmailMessageSendResult;
import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.parameter.RemoteEmailSendParameter;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮件发送器
 * @author shizhongming
 * 2024/10/31 9:13
 * @since 3.0.0
 */
public class SmartEmailSender implements SmartMessageSender {

    private static final Map<SmartMessageEmailChannelProperties, JavaMailSender> JAVA_MAIL_SENDER_MAP = new ConcurrentHashMap<>();

    /**
     * 获取支持的一级通道信息
     *
     * @return 支持的消息通道
     */
    @NonNull
    @Override
    public SmartMessageChannelType1Enum supportChannel1() {
        return SmartMessageChannelType1Enum.EMAIL;
    }

    /**
     * 发送消息
     *
     * @param channelProperties 通道参数
     * @param toUserList        用户列表
     * @param parameter         消息发送参数
     * @return 消息发送结果
     */
    @SneakyThrows(MessagingException.class)
    @Override
    public MessageSendResult send(@Nullable String channelProperties, List<SmartMessageToUserDTO> toUserList, RemoteMessageSendParameter parameter) {
        this.validateParameter(toUserList, parameter);

        SmartMessageEmailChannelProperties properties = JsonUtils.parse(channelProperties, SmartMessageEmailChannelProperties.class);

        JavaMailSender javaMailSender = this.getJavaMailSender(properties);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        List<String> toList = this.getToList(toUserList, parameter);

        RemoteEmailSendParameter emailSendParameter = parameter.getEmailSendParameter();

        helper.setFrom(emailSendParameter.getFrom());
        helper.setTo(toList.toArray(String[]::new));
        helper.setSubject(parameter.getTitle());
        // TODO: markdown 消息需要特殊处理,markdown转为html
        helper.setText(parameter.getContent(), this.isHtml(parameter));
        // 设置抄送人
        if (!CollectionUtils.isEmpty(emailSendParameter.getCcList())) {
            helper.setCc(emailSendParameter.getCcList().toArray(String[]::new));
        }
        // 发送附件
        if (!CollectionUtils.isEmpty(emailSendParameter.getAttachmentList())) {
            for (MultipartFile attachment : emailSendParameter.getAttachmentList()) {
                helper.addAttachment(Objects.requireNonNullElse(attachment.getOriginalFilename(), "attachment"), attachment);
            }
        }
        javaMailSender.send(mimeMessage);
        EmailMessageSendResult result = new EmailMessageSendResult();
        result.setSuccess(true);
        return result;
    }

    /**
     * 校验参数
     * @param toUserList 接收人
     */
    private void validateParameter(List<SmartMessageToUserDTO> toUserList, RemoteMessageSendParameter parameter) {
        List<String> toList = this.getToList(toUserList, parameter);
        if (CollectionUtils.isEmpty(toList)) {
            throw new SmartMessageException("收件人列表为空");
        }
        List<SmartMessageToUserDTO> nullEmailList = toUserList.stream()
                .filter(item -> !StringUtils.hasText(item.getEmail()))
                .toList();
        if (!CollectionUtils.isEmpty(nullEmailList)) {
            throw new SmartMessageException("发送邮件接收人email不能为空");
        }
        if (parameter.getEmailSendParameter() == null) {
            throw new SmartMessageException("邮件发送参数为空");
        }
        if (parameter.getEmailSendParameter().getFrom() == null) {
            throw new SmartMessageException("发件人不能为空");
        }
    }

    /**
     * 获取收件人列表
     * @param toUserList 接收人
     * @param parameter 发送参数
     * @return 收件人列表
     */
    private List<String> getToList(List<SmartMessageToUserDTO> toUserList, RemoteMessageSendParameter parameter) {
        if (CollectionUtils.isEmpty(toUserList)) {
            return parameter.getEmailSendParameter().getToList();
        }
        return toUserList.stream().map(SmartMessageToUserDTO::getEmail).toList();
    }

    /**
     * 获取 JavaMailSender
     * @param channelProperties 通道参数
     * @return JavaMailSender
     */
    private JavaMailSender getJavaMailSender(SmartMessageEmailChannelProperties channelProperties) {
        return JAVA_MAIL_SENDER_MAP.computeIfAbsent(channelProperties, properties -> {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(properties.getHost());
            if (properties.getPort() != null) {
                javaMailSender.setPort(properties.getPort());
            }
            javaMailSender.setUsername(properties.getUsername());
            javaMailSender.setPassword(properties.getPassword());
            javaMailSender.setProtocol(properties.getProtocol());

            if (properties.getDefaultEncoding() != null) {
                javaMailSender.setDefaultEncoding(properties.getDefaultEncoding().name());
            }

            String propertiesStr = channelProperties.getProperties();
            if (StringUtils.hasText(propertiesStr)) {
                Map<String, String> propertiesMap = JsonUtils.parse(propertiesStr, new TypeReference<>() {
                });
                javaMailSender.setJavaMailProperties(asProperties(propertiesMap));
            }

            return javaMailSender;
        });
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }
}
