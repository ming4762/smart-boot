package com.smart.monitor.server.core.client.repository;

import com.smart.monitor.server.common.MonitorServerProperties;
import com.smart.monitor.server.common.constants.ClientStatusEnum;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.model.ClientId;
import com.smart.starter.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;

/**
 * @author ShiZhongMing
 * 2022/10/25
 * @since 3.0.0
 */
@SuppressWarnings("unchecked")
@Slf4j
public class RedisClientRepositoryImpl extends AbstractClientRepositoryImpl {


    private static final String KEY_SPLIT = "@@";

    private final RedisService redisService;

    private final String storeKey;

    public RedisClientRepositoryImpl(RedisService redisService, MonitorServerProperties properties) {
        this.redisService = redisService;
        this.storeKey = properties.getClient().getStoreKey();
    }

    protected String getIdHashKey(String applicationName) {
        return applicationName + KEY_SPLIT + "id";
    }

    @Override
    public void save(@NonNull ClientData data) {
        this.redisService.hashPut(this.storeKey, data.getId().getValue(), data);
        // 保存name - id关系
        var idHashKey = this.getIdHashKey(data.getApplication().getApplicationName());

        var ids = (Set<String>) this.redisService.hashGet(this.storeKey, idHashKey);
        if (ids == null) {
            ids = new HashSet<>(1);
        }
        ids.add(data.getId().getValue());
        this.redisService.hashPut(this.storeKey, idHashKey, ids);
        this.refreshCacheTimeout();
    }

    @NonNull
    @Override
    public Collection<ClientData> findAll(boolean active) {
        return this.getActiveList(this.findAll());
    }

    @Override
    public Collection<ClientData> findAll() {
        var allData = this.redisService.hashEntries(this.storeKey);
        return allData.keySet().stream()
                .filter(item -> !item.toString().contains(KEY_SPLIT))
                .map(item -> (ClientData) allData.get(item))
                .toList();
    }


    @Nullable
    @Override
    public ClientData findById(@NonNull ClientId clientId, boolean active) {
        var data = this.findById(clientId);
        if (data == null) {
            return null;
        }
        if (active) {
            if (this.isActive(data)) {
                return data;
            }
            return null;
        }
        return data;
    }

    @Override
    public ClientData findById(@NonNull ClientId clientId) {
        return (ClientData) this.redisService.hashGet(this.storeKey, clientId.getValue());
    }

    @NonNull
    @Override
    public Collection<ClientData> findByName(@NonNull String name, boolean active) {
        return this.findByName(List.of(name), active);
    }

    @NonNull
    @Override
    public Collection<ClientData> findByName(@NonNull List<String> codeList, boolean active) {
        if (codeList.isEmpty()) {
            return new ArrayList<>(0);
        }
        // 1、获取名字对应的ID
        var clientIds = this.getClientIdsByName(codeList);
        // 2、查询列表
        if (clientIds.isEmpty()) {
            return new ArrayList<>(0);
        }
        var clientDataList = clientIds.stream()
                .map(id -> (ClientData) this.redisService.hashGet(this.storeKey, id))
                .toList();
        // 3、active
        if (active) {
            return this.getActiveList(clientDataList);
        }
        return clientDataList;
    }

    @Override
    public ClientData remove(@NonNull ClientId clientId) {
        // 1、查询信息
        var clientData = (ClientData) this.redisService.hashGet(this.storeKey, clientId.getValue());
        if (clientData == null) {
            return null;
        }
        // 2、删除信息
        var deleteKeys = new ArrayList<>();
        deleteKeys.add(clientId.getValue());
        // 3、删除id信息
        var idHashKey = this.getIdHashKey(clientData.getApplication().getApplicationName());
        var ids = (Set<String>) this.redisService.hashGet(this.storeKey, idHashKey);
        ids.remove(clientId.getValue());
        if (!ids.isEmpty()) {
            this.redisService.hashPut(this.storeKey, idHashKey, ids);
        } else {
            deleteKeys.add(idHashKey);
        }
        this.redisService.hashDelete(this.storeKey, deleteKeys);
        return clientData;
    }

    @Override
    public ClientData compute(ClientId id, BiFunction<ClientId, ClientData, ClientData> function) {
        var repositoryData = this.findById(id);
        var clientData = function.apply(id, repositoryData);
        if (clientData == null) {
            return null;
        }
        if (!clientData.equals(repositoryData)) {
            // 数据不相等，执行保存操作
            this.remove(id);
            this.save(clientData);
        }
        return clientData;
    }

    @Override
    public void clear() {
        this.redisService.delete(this.storeKey);
    }

    @Override
    public void removeByCode(String name) {
        var deleteKeys = this.getClientIdsByName(List.of(name));
        deleteKeys.add(this.getIdHashKey(name));
        this.redisService.hashDelete(this.storeKey, new ArrayList<>(deleteKeys));
    }

    @Override
    public void down(ClientId clientId) {
        var clientData = this.findById(clientId);
        if (clientData != null) {
            clientData.setStatus(ClientStatusEnum.DOWN);
            this.updateClientData(clientData);
        }
    }

    @Override
    public void up(ClientId id) {
        var clientData = this.findById(id);
        if (clientData != null) {
            clientData.setStatus(ClientStatusEnum.UP);
            this.updateClientData(clientData);
        }
    }

    @Override
    public void error(ClientId id, String errorMessage) {
        var clientData = this.findById(id);
        if (clientData != null) {
            clientData.setStatus(ClientStatusEnum.ERROR);
            clientData.setErrorMessage(errorMessage);
            this.updateClientData(clientData);
        }
    }

    @Override
    public void updateTimestamp(ClientId clientId) {
        var clientData = this.findById(clientId);
        if (clientData != null) {
            clientData.setTimestamp(Instant.now());
            this.updateClientData(clientData);
        }
    }

    protected Set<String> getClientIdsByName(List<String> applicationNameList) {
        var clientIds = new HashSet<String>();
        applicationNameList.forEach(name -> {
            var idHashKey = this.getIdHashKey(name);
            var ids = (Set<String>) this.redisService.hashGet(this.storeKey, idHashKey);
            if (!CollectionUtils.isEmpty(ids)) {
                clientIds.addAll(ids);
            }
        });
        return clientIds;
    }


    protected void updateClientData(ClientData clientData) {
        this.redisService.hashPut(this.storeKey, clientData.getId().getValue(), clientData);
        this.refreshCacheTimeout();
    }

    protected void refreshCacheTimeout() {
        // 缓存有效期设为10分钟
        this.redisService.expire(this.storeKey, Duration.ofSeconds(600));
    }
}
