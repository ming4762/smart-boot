package com.smart.monitor.server.core.notify.mail;

import com.smart.monitor.server.common.MonitorServerProperties;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.event.MonitorEvent;
import com.smart.monitor.server.core.notify.AbstractEventNotifier;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Set;

/**
 * 邮件事件通知接口
 * @author ShiZhongMing
 * 2022/2/18
 * @since 2.0.0
 */
@Slf4j
public class MailEventNotifier extends AbstractEventNotifier {


    private final MonitorServerProperties.MailNotify mailNotifyProperties;

    private TemplateEngine templateEngine;

    private JavaMailSender mailSender;

    public MailEventNotifier(MonitorServerProperties.MailNotify mailNotifyProperties) {
        this.mailNotifyProperties = mailNotifyProperties;
    }

    @SneakyThrows({MessagingException.class})
    @Override
    protected void doNotify(@NonNull MonitorEvent<?> event) {
        ClientData clientData = event.getClientData();
        Set<String> toList = clientData.getNotifyMails();
        if (CollectionUtils.isEmpty(toList)) {
            log.debug("mail to list is empty, application name: {}, event code: {}", clientData.getApplication().getApplicationName(), event.getCode());
        }
        Context context = new Context();
        context.setVariable("data", clientData);
        context.setVariable("event", event);

        String emailContext = this.templateEngine.process(mailNotifyProperties.getTemplate(), context);
        // 创建message
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(mailNotifyProperties.getFrom());
        helper.setTo(toList.toArray(new String[]{}));

        helper.setSubject(String.format("客户端【%s】触发事件（ID:%s，事件编码：%s）", clientData.getApplication().getApplicationName(), clientData.getId().getValue(), event.getCode()));
        helper.setText(emailContext, true);

        // 发送邮件
        this.mailSender.send(message);
    }

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
