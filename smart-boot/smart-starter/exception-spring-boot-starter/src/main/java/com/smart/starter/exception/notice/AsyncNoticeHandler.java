package com.smart.starter.exception.notice;

import com.smart.auth.core.userdetails.RestUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 异步通知
 * @author shizhongming
 * 2020/11/15 12:21 上午
 */
@Async
public class AsyncNoticeHandler implements ApplicationContextAware {

    /**
     * spring 上下文
     */
    private ApplicationContext applicationContext;


    /**
     * 进行异常通知
     * @param e 异常信息
     * @param user 用户信息
     * @param request 请求信息
     */
    public void noticeException(Exception e, RestUserDetails user, HttpServletRequest request) {
        // 获取所有通知器
        Map<String, ExceptionNotice> noticeMap = applicationContext.getBeansOfType(ExceptionNotice.class);
        // 执行通知
        noticeMap.forEach((key, notice) -> notice.notice(e, user, request));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
