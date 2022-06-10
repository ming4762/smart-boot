package com.smart.starter.exception.notice;

import com.smart.auth.core.userdetails.RestUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 实现 include 和 exclude
 * @author ShiZhongMing
 * 2021/3/5 11:34
 * @since 1.0
 */
public abstract class AbstractExceptionNotice implements ExceptionNotice {

    @Override
    public void notice(@NonNull Exception e, long exceptionNo, RestUserDetails user, @NonNull HttpServletRequest request) {
        if (this.isInclude(e) && !this.isExclude(e)) {
            this.doNotice(e, exceptionNo, user, request);
        }
    }

    /**
     * 是否包含在通知列表内
     * @param e 异常
     * @return 结果
     */
    protected boolean isInclude(@NonNull Exception e) {
        final List<Class<? extends Exception>> classList = this.include();
        if (classList == null) {
            return true;
        }
        return classList.stream().anyMatch(item -> item.equals(e.getClass()));
    }

    /**
     * 是否在排除列表内
     * @param e 异常
     * @return 结果
     */
    protected boolean isExclude(@NonNull Exception e) {
        return this.exclude().stream().anyMatch(item -> item.equals(e.getClass()));
    }


    /**
     * 进行通知
     * @param e 异常信息
     * @param exceptionNo 异常编号
     * @param user 用户
     * @param request 请求信息
     */
    protected abstract void doNotice(@NonNull Exception e, long exceptionNo, RestUserDetails user, @NonNull HttpServletRequest request);
}
