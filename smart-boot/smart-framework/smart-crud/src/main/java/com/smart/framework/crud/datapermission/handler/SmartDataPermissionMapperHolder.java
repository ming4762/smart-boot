package com.smart.framework.crud.datapermission.handler;

import com.smart.module.api.crud.module.SmartDataPermissionModel;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 数据权限Mapper缓存
 * @author shizhongming
 * 2025/3/17 20:05
 * @since 5.0.0
 */
public class SmartDataPermissionMapperHolder {

    private SmartDataPermissionMapperHolder() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, List<SmartDataPermissionModel>> DATA_PERMISSION_MAPPER_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, List<SmartDataPermissionModel>> DATA_PERMISSION_CODE_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据mapperId获取数据权限
     * @param mapperId mapperId
     * @param supplier 数据权限提供者
     * @return 数据权限
     */
    public static List<SmartDataPermissionModel> getCacheByMapperId(String mapperId, Supplier<List<SmartDataPermissionModel>> supplier) {
        return DATA_PERMISSION_MAPPER_CACHE.computeIfAbsent(mapperId, k -> supplier.get());
    }

    public static List<SmartDataPermissionModel> getCacheByCode(List<String> codeList, Function<List<String>, List<SmartDataPermissionModel>> handler) {
        if (CollectionUtils.isEmpty(codeList)) {
            return Collections.emptyList();
        }
        Map<Boolean, List<String>> cachedMap = codeList.stream()
                .collect(Collectors.groupingBy(DATA_PERMISSION_CODE_CACHE::containsKey));
        // 获取未缓存的code
        List<String> noCachedCodeList = cachedMap.get(Boolean.FALSE);
        if (CollectionUtils.isEmpty(noCachedCodeList)) {
            // 没有未缓存的数据说明已全部缓存，直接返回
            return getCacheByCode(codeList);
        }
        synchronized (DATA_PERMISSION_CODE_CACHE) {
            // 查询未缓存数据并缓存
            Map<String, List<SmartDataPermissionModel>> permissionCodeMap = Objects.requireNonNullElseGet(handler.apply(noCachedCodeList),  () -> new ArrayList<SmartDataPermissionModel>(0))
                    .stream()
                    .collect(Collectors.groupingBy(SmartDataPermissionModel::getPermissionCode));
            noCachedCodeList.forEach(item -> DATA_PERMISSION_CODE_CACHE.put(item, permissionCodeMap.getOrDefault(item, Collections.emptyList())));
        }
        return getCacheByCode(codeList);
    }

    private static List<SmartDataPermissionModel> getCacheByCode(List<String> codeList) {
        return codeList.stream()
                .flatMap(code -> DATA_PERMISSION_CODE_CACHE.getOrDefault(code, Collections.emptyList()).stream())
                .distinct()
                .toList();
    }

}
