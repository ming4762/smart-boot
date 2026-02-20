package com.smart.framework.crud.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 分页缓存
 * @author shizhongming
 * 2021/4/18 4:13 下午
 */
public class CrudPageHelper {

    private static final ScopedValue<Page<?>> PAGE_SCOPED_VALUE = ScopedValue.newInstance();

    private CrudPageHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T withPage(Page<?> page, Supplier<T> supplier) {
        AtomicReference<T> result = new AtomicReference<>();
        ScopedValue.where(PAGE_SCOPED_VALUE, page).run(() -> result.set(supplier.get()));
        return result.get();
    }

    public static <T> Page<T> get() {
        return (Page<T>) PAGE_SCOPED_VALUE.get();
    }

    /**
     * 是否存在 Page（防御性）
     */
    public static boolean exists() {
        return PAGE_SCOPED_VALUE.get() != null;
    }
}
