package com.smart.framework.exception.notice;

import com.smart.framework.commons.core.auth.TokenHolder;
import com.smart.framework.commons.core.utils.IpUtils;
import com.smart.framework.exception.pojo.dto.ExceptionNoticeDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 异步通知
 * @author shizhongming
 * 2020/11/15 12:21 上午
 */
@Slf4j
public class AsyncNoticeHandler implements ApplicationContextAware {

    private static final ThreadLocal<Boolean> IN_EXCEPTION_HANDLER = ThreadLocal.withInitial(() -> false);

    private List<ExceptionNotice> exceptionNoticeList;

    /**
     * 进行异常通知
     * @param e 异常信息
     * @param request 请求信息
     */
    public void noticeException(Exception e, long exceptionNo, HttpServletRequest request) {
        // 执行通知
        // 防止递归处理
        if (CollectionUtils.isEmpty(exceptionNoticeList)) {
            return;
        }
        if (Boolean.TRUE.equals(IN_EXCEPTION_HANDLER.get())) {
            log.warn("Skipping recursive exception handling: {}", e.getMessage(), e);
        }
        try {
            ExceptionNoticeDTO exceptionData = ExceptionNoticeDTO.builder()
                    .exception(e)
                    .exceptionNo(exceptionNo)
                    .requestIp(IpUtils.getIpAddr(request))
                    .requestPath(request.getServletPath())
                    .build();
            IN_EXCEPTION_HANDLER.set(true);
            String token = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .map(ServletRequestAttributes::getRequest)
                    .map(item -> item.getHeader(HttpHeaders.AUTHORIZATION))
                    .orElse(null);
            CompletableFuture.runAsync(new DelegatingSecurityContextRunnable(() -> {
                TokenHolder.set(token);
                exceptionNoticeList.forEach(item -> {
                    try {
                        item.notice(exceptionData);
                    } catch (Exception exception) {
                        log.error(exception.getMessage(), exception);
                    }
                });
            }));
        } finally {
            IN_EXCEPTION_HANDLER.remove();
            TokenHolder.clear();
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.exceptionNoticeList = Arrays.stream(applicationContext.getBeanNamesForType(ExceptionNotice.class))
                .map(name -> applicationContext.getBean(name, ExceptionNotice.class))
                .toList();
    }
}
