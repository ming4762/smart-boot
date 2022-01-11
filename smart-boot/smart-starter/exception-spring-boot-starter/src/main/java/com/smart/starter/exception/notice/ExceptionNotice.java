package com.smart.starter.exception.notice;

import com.smart.auth.core.userdetails.RestUserDetails;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常通知接口
 * @author shizhongming
 * 2020/11/15 12:08 上午
 */
public interface ExceptionNotice {

    /**
     * 异常通知
     * @param e 异常信息
     * @param user 用户信息
     * @param request 请求信息
     */
    void notice(@NonNull Exception e, RestUserDetails user, @NonNull HttpServletRequest request);

    /**
     * 包含的异常进行通知
     * 如果返回null，则通知所有异常
     * @return 需要通知的异常
     */
    @Nullable
    default List<Class<? extends Exception>> include() {
        return null;
    }

    /**
     * 不需要通知的异常
     * @return 不需要通知的异常
     *
     */
    @NonNull
    default List<Class<? extends Exception>> exclude() {
        return new ArrayList<>(0);
    }
}
