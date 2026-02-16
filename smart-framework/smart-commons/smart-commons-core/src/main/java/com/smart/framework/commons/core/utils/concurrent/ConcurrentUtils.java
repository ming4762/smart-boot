package com.smart.framework.commons.core.utils.concurrent;

import lombok.SneakyThrows;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 并发工具类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-01-04 16:29
 * @since 5.0.0
 */
public class ConcurrentUtils {

    private ConcurrentUtils() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * 并发执行所有任务，允许部分任务失败
     * 使用虚拟线程
     * @param taskList 任务列表
     * @param timeout 超时时间，null 表示不超时
     * @return 任务索引到任务结果的映射
     * @param <T> 任务返回类型
     */
    @SneakyThrows(Exception.class)
    public static <T> Map<Integer, InvokeResult<T>> invokeWithVirtualThread(List<Callable<T>> taskList, @Nullable Duration timeout) {
        Map<Integer, Callable<T>> taskMap = HashMap.newHashMap(taskList.size());
        for (int i = 0; i < taskList.size(); i++) {
            taskMap.put(i, taskList.get(i));
        }
        return invokeWithVirtualThread(taskMap, timeout);
    }

    /**
     * 并发执行所有任务，允许部分任务失败
     * 使用虚拟线程
     * @param tasksMap 任务列表
     * @param timeout 超时时间，null 表示不超时
     * @return 任务索引到任务结果的映射
     * @param <T> 任务返回类型
     */
    @SneakyThrows(Exception.class)
    public static <K extends Serializable, T> Map<K, InvokeResult<T>> invokeWithVirtualThread(Map<K, Callable<T>> tasksMap, @Nullable Duration timeout) {
        if (CollectionUtils.isEmpty(tasksMap)) {
            return Map.of();
        }
        Map<K, Future<InvokeResult<T>>> futureMap = HashMap.newHashMap(tasksMap.size());
        Map<K, InvokeResult<T>> resultMap = HashMap.newHashMap(tasksMap.size());

        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {
            for (var entry : tasksMap.entrySet()) {
                futureMap.put(entry.getKey(), executor.submit(() -> safeCall(entry.getValue())));
            }

            // 收集结果（逐个超时，不连坐）
            for (var entry : futureMap.entrySet()) {
                try {
                    if (timeout == null) {
                        resultMap.put(
                                entry.getKey(),
                                entry.getValue().get()
                        );
                    } else {
                        resultMap.put(
                                entry.getKey(),
                                entry.getValue().get(timeout.toMillis(), TimeUnit.MILLISECONDS)
                        );
                    }
                } catch (TimeoutException e) {
                    resultMap.put(
                            entry.getKey(),
                            InvokeResult.failure(e)
                    );
                }
            }
        } catch (InterruptedException e) {
            // 不向外传播 interrupt，防止 IO 中断
            Thread.currentThread().interrupt();
        }

        return resultMap;
    }

    private static <T> InvokeResult<T> safeCall(Callable<T> task) {
        try {
            return InvokeResult.success(task.call());
        } catch (Exception e) {
            return InvokeResult.failure(e);
        }
    }

    public record InvokeResult<T>(
            boolean success,
            T data,
            Throwable error
    ) {
        public static <T> InvokeResult<T> success(T data) {
            return new InvokeResult<>(true, data, null);
        }

        public static <T> InvokeResult<T> failure(Throwable error) {
            return new InvokeResult<>(false, null, error);
        }
    }
}
