package com.smart.monitor.server.core.notify.mail;

import com.smart.monitor.server.common.MonitorServerProperties;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.event.MonitorEvent;
import com.smart.monitor.server.core.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * 邮件事件通知接口
 * @author ShiZhongMing
 * 2022/2/18
 * @since 2.0.0
 */
@Slf4j
public class MailEventNotifier extends AbstractEventNotifier {


    @Override
    protected void doNotify(@NonNull MonitorEvent<?> event) {
        ClientData clientData = event.getClientData();
        Set<String> toList = clientData.getNotifyMails();
        if (CollectionUtils.isEmpty(toList)) {
            log.debug("mail to list is empty, application name: {}, event code: {}", clientData.getApplication().getApplicationName(), event.getCode());
        }

    }
}
