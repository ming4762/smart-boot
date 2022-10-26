package com.smart.monitor.server.core.client.repository;

import com.smart.monitor.server.common.constants.ClientStatusEnum;
import com.smart.monitor.server.common.model.ClientData;
import org.springframework.lang.NonNull;

import java.util.Collection;

/**
 * @author ShiZhongMing
 * 2021/3/22 13:41
 * @since 1.0
 */
public abstract class AbstractClientRepositoryImpl implements ClientRepository {

    /**
     * 判断是否激活状态
     * @param repositoryData 客户端信息
     * @return 状态
     */
    protected boolean isActive(@NonNull ClientData repositoryData) {
        return ClientStatusEnum.UP.equals(repositoryData.getStatus());
    }

    protected Collection<ClientData> getActiveList(Collection<ClientData> repositoryDatas) {
        return repositoryDatas.stream().filter(this::isActive).toList();
    }
}
