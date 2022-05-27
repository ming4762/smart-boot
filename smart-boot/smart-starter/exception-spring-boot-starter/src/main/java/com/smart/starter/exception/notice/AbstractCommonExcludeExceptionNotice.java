package com.smart.starter.exception.notice;

import com.google.common.collect.Lists;
import com.smart.commons.core.exception.BusinessException;
import com.smart.commons.core.i18n.I18nException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

/**
 * 排除通用异常
 * @author ShiZhongMing
 * 2021/3/5 11:44
 * @since 1.0
 */
public abstract class AbstractCommonExcludeExceptionNotice extends AbstractExceptionNotice {


    @Override
    @NonNull
    public List<Class<? extends Exception>> exclude() {
        return Lists.newArrayList(
                // 参数校验类异常不通知
                BindException.class,
                MethodArgumentNotValidException.class,
                ConstraintViolationException.class,
                // 业务异常不通知
                BusinessException.class,
                I18nException.class
        );
    }
}
