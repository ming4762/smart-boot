package com.smart.framework.crud.datapermission.handler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.smart.module.api.crud.module.SmartDataPermissionModel;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
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

    private static final Map<String, List<SmartDataPermissionModel>> DATA_PERMISSION_CODE_CACHE = new ConcurrentHashMap<>();

    /**
     * 存储当前用户数据权限
     */
    private static final Cache<String, Map<String, List<SmartDataPermissionModel>>> USER_DATA_PERMISSION_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofHours(5))
            .build();

    /**
     * 根据mapperId获取数据权限
     * @param mapperId mapperId
     * @param supplier 数据权限提供者
     * @return 数据权限
     */
    public static List<SmartDataPermissionModel> getCacheByMapperId(String token, String mapperId, Supplier<List<SmartDataPermissionModel>> supplier) {
        Map<String, List<SmartDataPermissionModel>> cacheData = USER_DATA_PERMISSION_CACHE.getIfPresent(token);
        if (cacheData == null) {
            List<SmartDataPermissionModel> dataPermissionModelList = supplier.get();
            if (CollectionUtils.isEmpty(dataPermissionModelList)) {
                USER_DATA_PERMISSION_CACHE.put(token, Collections.emptyMap());
                return Collections.emptyList();
            }
            Map<String, List<SmartDataPermissionModel>> mapperDataPermissionMap = dataPermissionModelList.stream()
                    .filter(item -> item.getMapperStatementId() != null)
                    .collect(Collectors.groupingBy(SmartDataPermissionModel::getMapperStatementId));
            USER_DATA_PERMISSION_CACHE.put(token, mapperDataPermissionMap);
            return mapperDataPermissionMap.getOrDefault(mapperId, Collections.emptyList());
        }
        return cacheData.getOrDefault(mapperId, Collections.emptyList());
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

    /**
     * 清理缓存
     */
    public static void clear() {
        DATA_PERMISSION_CODE_CACHE.clear();
    }

}
