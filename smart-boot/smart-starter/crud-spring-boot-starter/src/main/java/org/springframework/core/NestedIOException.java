package org.springframework.core;

import java.io.IOException;
import java.io.Serial;

/**
 * 兼容 mybatis-plus
 * spring6移除了该异常，会导致mybatis-plus {@link com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean} ClassNotFoundException
 * @author ShiZhongMing
 * 2022/8/30
 * @since 3.0.0
 */
public class NestedIOException extends IOException {
    @Serial
    private static final long serialVersionUID = 7152460873667450478L;

    public NestedIOException(String message) {
        super(message);
    }

    public NestedIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public NestedIOException(Throwable cause) {
        super(cause);
    }
}
