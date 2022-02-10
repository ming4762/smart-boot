package com.smart.monitor.server.core.client.repository;

import com.smart.monitor.core.model.Application;
import com.smart.monitor.server.common.client.ClientManagerProvider;
import com.smart.monitor.server.common.constants.ClientStatusEnum;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.model.ClientId;
import com.smart.monitor.server.common.model.ClientManagerData;
import com.smart.monitor.server.core.client.ClientIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 存储在客户端的信息
 * @author shizhongming
 * 2021/3/21 11:16 下午
 */
@Slf4j
public class MemoryClientRepositoryImpl extends AbstractClientRepositoryImpl {

    private final ClientIdGenerator clientIdGenerator;

    private final ClientManagerProvider clientManagerProvider;

    /**
     * 存储客户端信息
     */
    private final Map<ClientId, ClientData> repositoryDataMap = new ConcurrentHashMap<>();

    public MemoryClientRepositoryImpl(ClientIdGenerator clientIdGenerator, ClientManagerProvider clientManagerProvider) {
        this.clientIdGenerator = clientIdGenerator;
        this.clientManagerProvider = clientManagerProvider;
    }

    @Nullable
    @Override
    public ClientData save(@NonNull Application application) {
        final ClientManagerData clientManager = this.clientManagerProvider.getByName(application.getApplicationName());
        if (clientManager == null) {
            log.warn("client is not manager，please manager it，application code: {}", application.getApplicationName());
            return null;
        }
        final ClientId clientId = this.clientIdGenerator.create(application);
        final ClientData data = new ClientData(application, clientId, clientManager);
        this.repositoryDataMap.put(clientId, data);
        return data;
    }

    @NonNull
    @Override
    public Collection<ClientData> findAll(boolean active) {
        final Collection<ClientData> allList = this.repositoryDataMap.values();
        if (active) {
            return this.getActiveList(allList);
        }
        return allList;
    }

    @Override
    public ClientData findById(@NonNull ClientId clientId, boolean active) {
        final ClientData repositoryData = this.repositoryDataMap.get(clientId);
        if (repositoryData == null || (active && !this.isActive(repositoryData))) {
            return null;
        }
        return repositoryData;
    }

    @NonNull
    @Override
    public Collection<ClientData> findByName(@NonNull String name, boolean active) {
        return this.findAll(active).stream()
                .filter(item -> item.getApplication().getApplicationName().equals(name))
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public Collection<ClientData> findByName(@NonNull List<String> codeList, boolean active) {
        if (codeList.isEmpty()) {
            return new ArrayList<>(0);
        }
        return this.findAll(active).stream()
                .filter(item -> codeList.contains(item.getApplication().getApplicationName()))
                .collect(Collectors.toList());
    }

    @Override
    public ClientData remove(@NonNull ClientId clientId) {
        return this.repositoryDataMap.remove(clientId);
    }

    @Override
    public ClientData compute(ClientId id, BiFunction<ClientId, ClientData, ClientData> function) {
        ClientData repositoryData = function.apply(id, this.repositoryDataMap.get(id));
        if (repositoryData == null) {
            return null;
        }
        this.repositoryDataMap.put(id, repositoryData);
        return repositoryData;
    }

    @Override
    public void clear() {
        this.repositoryDataMap.clear();
    }

    @Override
    public void removeByCode(String name) {
        final Collection<ClientData> repositoryList = this.findByName(name, false);
        repositoryList.forEach(item -> this.repositoryDataMap.remove(item.getId()));
    }

    @Override
    public void down(ClientId clientId) {
        this.compute(clientId, (id, repositoryData) -> {
            if (repositoryData != null) {
                repositoryData.setStatus(ClientStatusEnum.DOWN);
            }
            return repositoryData;
        });
    }

    @Override
    public void up(ClientId clientId) {
        this.compute(clientId, (id, repositoryData) -> {
            if (repositoryData != null) {
                repositoryData.setStatus(ClientStatusEnum.UP);
            }
            return repositoryData;
        });
    }

    @Override
    public void updateTimestamp(ClientId clientId) {
        this.compute(clientId, (id, repositoryData) -> {
            if (repositoryData != null) {
                repositoryData.setTimestamp(Instant.now());
            }
            return repositoryData;
        });
    }


    private Collection<ClientData> getActiveList(Collection<ClientData> repositoryDatas) {
        return repositoryDatas.stream().filter(this::isActive).collect(Collectors.toList());
    }
}
