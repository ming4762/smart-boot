package com.smart.framework.commons.core.function;

/**
 * 可以抛出异常的Runnable
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-14 23:31
 * @since 5.0.0
 */
@FunctionalInterface
public interface ThrowingRunnable {

    void run() throws Exception;
}
