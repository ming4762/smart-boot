package com.smart.framework.exception.notice;

import com.smart.framework.exception.pojo.dto.ExceptionNoticeDTO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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
     * @param exceptionData 异常信息
     */
    void notice(@NonNull ExceptionNoticeDTO exceptionData);

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
