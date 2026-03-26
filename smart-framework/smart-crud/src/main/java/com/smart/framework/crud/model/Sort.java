package com.smart.framework.crud.model;

import org.jspecify.annotations.NonNull;

/**
 * 排序字段
 *
 * @param dbName 对应的数据库字段
 * @author shizhongming
 * 2020/1/12 4:13 下午
 */
public record Sort(@NonNull String name, boolean asc, @NonNull String dbName) {
}
