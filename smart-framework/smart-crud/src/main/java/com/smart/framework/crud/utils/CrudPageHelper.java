package com.smart.framework.crud.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.framework.crud.model.BaseModel;
import com.smart.framework.crud.model.Sort;
import com.smart.framework.crud.query.PageSortQuery;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
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
        return PAGE_SCOPED_VALUE.isBound() && PAGE_SCOPED_VALUE.get() != null;
    }

    /**
     * 创建分页参数
     * @param entityClass 实体类
     * @param parameter 分页参数
     * @return 分页参数
     * @param <P> 分页结果类型
     */
    public static <P> Page<P> createPage(Class<? extends BaseModel> entityClass, PageSortQuery parameter) {
        return createPage(entityClass, parameter.getPageSize(), parameter.getCurrentPage(), parameter.getSortName(), parameter.getSortOrder());
    }

    /**
     * 创建分页参数
     * @param entityClass 实体类
     * @param pageSize 分页大小
     * @param currentPage 当前页
     * @param sortName 排序字段
     * @param sortOrder 排序方向
     * @return 分页参数
     * @param <P> 分页结果类型
     */
    public static <P> Page<P> createPage(@NonNull Class<? extends BaseModel> entityClass, @Nullable Integer pageSize, @Nullable Integer currentPage, @Nullable String sortName, @Nullable String sortOrder) {
        if (pageSize == null) {
            return null;
        }
        Page<P> page = Page.of(Objects.requireNonNullElse(currentPage, 1), pageSize);
        List<OrderItem> orderItemList = analysisOrder(entityClass, sortName, sortOrder);
        if (Objects.nonNull(orderItemList)) {
            page.setOrders(orderItemList);
        }
        return page;
    }

    /**
     * 分析排序参数
     * @param entityClass 实体类
     * @param sortName 排序字段
     * @param sortOrder 排序方向
     * @return 排序参数
     */
    @org.jspecify.annotations.Nullable
    private static List<OrderItem> analysisOrder(@NonNull Class<? extends BaseModel> entityClass, @Nullable String sortName, @Nullable String sortOrder) {
        if (!StringUtils.hasLength(sortName)) {
            return null;
        }
        final List<Sort> sortList = CrudUtils.analysisOrder(sortName, sortOrder, entityClass);
        if (sortList.isEmpty()) {
            return null;
        }
        return sortList
                .stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setColumn(item.dbName());
                    orderItem.setAsc(item.asc());
                    return orderItem;
                }).toList();
    }
}
