package com.smart.crud.utils;

import com.github.pagehelper.Page;

/**
 * 分页缓存
 * @author shizhongming
 * 2021/4/18 4:13 下午
 */
public class PageCache {

    private static final ThreadLocal<Page<?>> PAGE_THREAD_LOCAL = new ThreadLocal<>();

    private PageCache() {
        throw new IllegalStateException("Utility class");
    }

    public static void set(Page<?> page) {
        PAGE_THREAD_LOCAL.remove();
        PAGE_THREAD_LOCAL.set(page);
    }

    public static Page<?> get() {
        return PAGE_THREAD_LOCAL.get();
    }
}
