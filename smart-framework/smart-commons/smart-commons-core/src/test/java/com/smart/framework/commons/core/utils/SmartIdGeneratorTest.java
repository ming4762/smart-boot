package com.smart.framework.commons.core.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SmartIdGenerator测试类
 * @author shizhongming
 * 2025/8/20 18:13
 * @since 5.0.0
 */
class SmartIdGeneratorTest {

    // 测试正常ID生成
    @Test
    void testNextIdNormal() {
        long id1 = SmartIdGenerator.nextId();
        long id2 = SmartIdGenerator.nextId();
        assertTrue(id1 < id2, "ID应该递增");
    }

    // 测试偏移时间戳计算
    @Test
    @EnabledIfSystemProperty(named = "smart.id.year", matches = "2020")
    void testOffsetWithCustomYear() {
        long expectedOffset = LocalDate.of(2020, 1, 1)
                .atStartOfDay(ZoneId.of("Z"))
                .toEpochSecond();

        // 通过反射获取实际偏移值
        try {
            Field offsetField = SmartIdGenerator.class.getDeclaredField("OFFSET");
            offsetField.setAccessible(true);
            long actualOffset = (long) offsetField.get(null);
            assertEquals(expectedOffset, actualOffset);
        } catch (Exception e) {
            fail("获取OFFSET字段失败: " + e.getMessage());
        }
    }

    // 测试线程数量
    private static final int THREAD_COUNT = 50;
    // 每个线程生成的ID数量
    private static final int IDS_PER_THREAD = 10000;
    // 用于存储所有生成的ID，验证唯一性
    private static final Set<Long> idSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * 压力测试: 多线程并发生成ID
     */
    @Test
    void testNextIdUnderPressure() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        long startTime = System.currentTimeMillis();

        // 提交任务到线程池
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < IDS_PER_THREAD; j++) {
                        long id = SmartIdGenerator.nextId();
                        idSet.add(id);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待所有线程完成
        latch.await();
        long endTime = System.currentTimeMillis();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        // 计算并输出测试结果
        long totalTime = endTime - startTime;
        long totalIdsGenerated = THREAD_COUNT * IDS_PER_THREAD;
        double throughput = (totalIdsGenerated * 1000.0) / totalTime;

        System.out.println("=== 压力测试结果 ===");
        System.out.println("总生成ID数: " + totalIdsGenerated);
        System.out.println("总耗时: " + totalTime + " ms");
        System.out.println("吞吐量: " + String.format("%.2f", throughput) + " IDs/秒");
        System.out.println("是否存在重复ID: " + (idSet.size() != totalIdsGenerated));
        System.out.println("====================");
    }
}