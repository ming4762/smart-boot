package com.smart.monitor.server.core.client.repository;

import com.smart.monitor.core.model.Application;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.model.ClientId;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 定义客户端仓库接口
 * @author shizhongming
 * 2021/3/21 10:53 下午
 */
public interface ClientRepository {

    /**
     * 保存
     * @param application 客户端信息
     * @return 客户端存储信息
     */
    @Nullable
    ClientData save(@NonNull Application application);

    /**
     * 查询所有
     * @param active 有效的
     * @return 所有客户端信息
     */
    @NonNull
    Collection<ClientData> findAll(boolean active);


    /**
     * 查询所有
     * @return 所有客户端信息
     */
    @NonNull
    default Collection<ClientData> findAll() {
        return this.findAll(true);
    }

    /**
     * 通过ID查询
     * @param clientId 客户端ID
     * @param active 是否只查询激活状态的客户端
     * @return 客户端信息
     */
    @Nullable
    ClientData findById(@NonNull ClientId clientId, boolean active);

    /**
     * 通过ID查询
     * @param clientId 客户端ID
     * @return 客户端信息
     */
    default ClientData findById(@NonNull ClientId clientId) {
        return this.findById(clientId, true);
    }


    /**
     * 通过编码查找
     * @param name 名字
     * @param active 有效的
     * @return 客户端信息
     */
    @NonNull
    Collection<ClientData> findByName(@NonNull String name, boolean active);

    /**
     * 通过编码集合查找
     * @param codeList 编码集合
     * @param active 有效的
     * @return 客户端信息
     */
    @NonNull
    Collection<ClientData> findByName(@NonNull List<String> codeList, boolean active);

    /**
     * 移除客户端
     * @param clientId 客户端ID
     * @return 移除的客户端信息
     */
    ClientData remove(@NonNull ClientId clientId);

    /**
     * 更新操作
     * @param id 客户端ID
     * @param function 回调函数
     * @return 客户端信息
     */
    ClientData compute(ClientId id, BiFunction<ClientId, ClientData, ClientData> function);

    /**
     * 清除仓库
     */
    void clear();

    /**
     * 通过名字移除
     * @param name 名字
     */
    void removeByCode(String name);

    /**
     * 下线
     * @param clientId 客户端ID
     */
    void down(ClientId clientId);

    /**
     * 上线
     * @param id 客户端ID
     */
    void up(ClientId id);

    /**
     * 更新时间戳
     * @param clientId 客户端
     */
    void updateTimestamp(ClientId clientId);
}
