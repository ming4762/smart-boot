package com.smart.starter.exception.notice;

import com.smart.auth.core.userdetails.RestUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;
import java.util.List;

/**
 * 异步通知
 * @author shizhongming
 * 2020/11/15 12:21 上午
 */
@Async
@Slf4j
public class AsyncNoticeHandler implements ApplicationContextAware {

    private List<ExceptionNotice> exceptionNoticeList;


    /**
     * 进行异常通知
     * @param e 异常信息
     * @param user 用户信息
     * @param request 请求信息
     */
    public void noticeException(Exception e, long exceptionNo, RestUserDetails user, HttpServletRequest request) {
        // 执行通知
        if (CollectionUtils.isNotEmpty(exceptionNoticeList)) {
            exceptionNoticeList.forEach(item -> {
                try {
                    item.notice(e, exceptionNo, user, request);
                } catch (Exception exception) {
                    log.error(exception.getMessage(), exception);
                }
            });
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.exceptionNoticeList = Arrays.stream(applicationContext.getBeanNamesForType(ExceptionNotice.class))
                .map(name -> applicationContext.getBean(name, ExceptionNotice.class))
                .toList();
    }
}
