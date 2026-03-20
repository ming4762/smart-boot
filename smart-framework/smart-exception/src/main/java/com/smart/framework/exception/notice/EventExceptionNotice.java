package com.smart.framework.exception.notice;

import com.smart.framework.commons.core.exception.SmartExceptionEventData;
import com.smart.framework.commons.core.utils.ExceptionUtils;
import com.smart.framework.exception.event.SmartExceptionEvent;
import com.smart.framework.exception.pojo.dto.ExceptionNoticeDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import java.net.InetAddress;

/**
 * 异常通知，异常通过事件通知
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-17 22:50
 * @since 5.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class EventExceptionNotice implements ExceptionNotice {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 异常通知
     *
     * @param exceptionData 异常信息
     */
    @Override
    public void notice(@NonNull ExceptionNoticeDTO exceptionData) {
        try {
            SmartExceptionEventData dto = SmartExceptionEventData.builder()
                    .exceptionNo(exceptionData.getExceptionNo())
                    .exceptionMessage(exceptionData.getException().toString())
                    .stackTrace(ExceptionUtils.throwableToString(exceptionData.getException(), true))
                    .requestIp(exceptionData.getRequestIp())
                    .serverIp(InetAddress.getLocalHost().getHostAddress())
                    .requestPath(exceptionData.getRequestPath())
                    .build();

            SmartExceptionEvent exceptionEvent = new SmartExceptionEvent(dto);
            applicationEventPublisher.publishEvent(exceptionEvent);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
