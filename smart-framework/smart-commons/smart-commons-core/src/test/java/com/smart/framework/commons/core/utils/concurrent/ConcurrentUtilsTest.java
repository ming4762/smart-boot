package com.smart.framework.commons.core.utils.concurrent;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 并发工具类测试类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-01-07 15:34
 * @since 5.0.0
 */
class ConcurrentUtilsTest {

    /**
     * 所有任务正常执行
     */
    @Test
    void testInvokeWithVirtualThread_allSuccess() {
        List<Callable<String>> tasks = List.of(
                () -> "A",
                () -> "B",
                () -> "C"
        );

        Map<Integer, ConcurrentUtils.InvokeResult<String>> result =
                ConcurrentUtils.invokeWithVirtualThread(tasks, Duration.ofSeconds(2));

        assertEquals(3, result.size());

        assertTrue(result.get(0).success());
        assertEquals("A", result.get(0).data());

        assertTrue(result.get(1).success());
        assertEquals("B", result.get(1).data());

        assertTrue(result.get(2).success());
        assertEquals("C", result.get(2).data());
    }

    /**
     * 部分任务抛异常，不影响其他任务
     */
    @Test
    void testInvokeWithVirtualThread_partialFailure() {
        List<Callable<String>> tasks = List.of(
                () -> "OK",
                () -> { throw new IllegalStateException("boom"); },
                () -> "SUCCESS"
        );

        Map<Integer, ConcurrentUtils.InvokeResult<String>> result =
                ConcurrentUtils.invokeWithVirtualThread(tasks, Duration.ofSeconds(2));

        assertEquals(3, result.size());

        assertTrue(result.get(0).success());
        assertEquals("OK", result.get(0).data());

        assertFalse(result.get(1).success());
        assertNotNull(result.get(1).error());
        assertInstanceOf(IllegalStateException.class, result.get(1).error());

        assertTrue(result.get(2).success());
        assertEquals("SUCCESS", result.get(2).data());
    }

    /**
     * 单个任务超时，不影响其他任务（不连坐）
     */
    @Test
    void testInvokeWithVirtualThread_timeoutIndividually() {
        List<Callable<String>> tasks = List.of(
                () -> {
                    Thread.sleep(300);
                    return "FAST";
                },
                () -> {
                    Thread.sleep(2000);
                    return "SLOW";
                }
        );

        Map<Integer, ConcurrentUtils.InvokeResult<String>> result =
                ConcurrentUtils.invokeWithVirtualThread(tasks, Duration.ofMillis(500));

        assertTrue(result.get(0).success());
        assertEquals("FAST", result.get(0).data());

        assertFalse(result.get(1).success());
        assertInstanceOf(
                java.util.concurrent.TimeoutException.class,
                result.get(1).error()
        );
    }

    /**
     * timeout = null 表示不超时
     */
    @Test
    void testInvokeWithVirtualThread_noTimeout() {
        List<Callable<String>> tasks = List.of(
                () -> {
                    Thread.sleep(300);
                    return "OK";
                }
        );

        Map<Integer, ConcurrentUtils.InvokeResult<String>> result =
                ConcurrentUtils.invokeWithVirtualThread(tasks, null);

        assertTrue(result.get(0).success());
        assertEquals("OK", result.get(0).data());
    }

    /**
     * 空任务列表
     */
    @Test
    void testInvokeWithVirtualThread_emptyList() {
        Map<Integer, ConcurrentUtils.InvokeResult<String>> result =
                ConcurrentUtils.invokeWithVirtualThread(Collections.emptyList(), Duration.ofSeconds(1));

        assertTrue(result.isEmpty());
    }

    /**
     * Map 版本：Key 映射正确
     */
    @Test
    void testInvokeWithVirtualThread_mapKeyPreserved() {
        Map<String, Callable<Integer>> tasks = new LinkedHashMap<>();
        tasks.put("A", () -> 1);
        tasks.put("B", () -> 2);

        Map<String, ConcurrentUtils.InvokeResult<Integer>> result =
                ConcurrentUtils.invokeWithVirtualThread(tasks, Duration.ofSeconds(1));

        assertEquals(2, result.size());
        assertEquals(1, result.get("A").data());
        assertEquals(2, result.get("B").data());
    }

    /**
     * 并发能力验证（不是串行）
     * 3 个 300ms 任务，总耗时应 < 600ms（虚拟线程并发）
     */
    @Test
    void testInvokeWithVirtualThread_parallelExecution() {
        List<Callable<String>> tasks = List.of(
                sleepTask(300),
                sleepTask(300),
                sleepTask(300)
        );

        long start = System.currentTimeMillis();

        ConcurrentUtils.invokeWithVirtualThread(tasks, Duration.ofSeconds(2));

        long cost = System.currentTimeMillis() - start;

        assertTrue(cost < 600,
                "Expected parallel execution, but cost=" + cost + "ms");
    }

    private Callable<String> sleepTask(long millis) {
        return () -> {
            Thread.sleep(millis);
            return "OK";
        };
    }
}
