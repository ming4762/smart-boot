package com.smart.framework.exception.notice;

import com.smart.framework.exception.pojo.dto.ExceptionNoticeDTO;
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
    public void notice(@NonNull ExceptionNoticeDTO exceptionData) {
        if (this.isInclude(exceptionData.getException()) && !this.isExclude(exceptionData.getException())) {
            this.doNotice(exceptionData);
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
        return classList.stream().anyMatch(item -> item.equals(e.getClass()) || item.isAssignableFrom(e.getClass()));
    }

    /**
     * 是否在排除列表内
     * @param e 异常
     * @return 结果
     */
    protected boolean isExclude(@NonNull Exception e) {
        return this.exclude().stream().anyMatch(item -> item.equals(e.getClass()) || item.isAssignableFrom(e.getClass()));
    }


    /**
     * 进行通知
     * @param e 异常信息
     * @param exceptionNo 异常编号
     * @param request 请求信息
     */
    protected abstract void doNotice(@NonNull ExceptionNoticeDTO exceptionData);
}
