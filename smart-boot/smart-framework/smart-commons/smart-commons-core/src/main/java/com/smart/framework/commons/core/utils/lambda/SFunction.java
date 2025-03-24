package com.smart.framework.commons.core.utils.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 函数式接口
 * @author shizhongming
 * 2025/3/24 9:37
 * @since 5.0.0
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
