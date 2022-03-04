package com.smart.monitor.server.common.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.commons.core.utils.json.InstantJson;
import com.smart.monitor.core.model.Application;
import com.smart.monitor.server.common.constants.ClientStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * 客户端注册数据
 * @author shizhongming
 * 2021/3/21 11:00 下午
 */
@Getter
@Setter
public final class ClientData implements Serializable {

    private static final long serialVersionUID = 4944979208990091705L;
    /**
     * 时间戳
     */
    @JsonSerialize(using = InstantJson.InstantSerializer.class)
    private Instant timestamp = Instant.now();

    /**
     * 刷新时间戳
     */
    @JsonSerialize(using = InstantJson.InstantSerializer.class)
    private Instant refreshTime;

    private Application application;

    private final ClientId id;

    /**
     * 状态监测时间间隔
     */
    private Duration statusInterval;

    /**
     * 序列化的事件编码
     */
    @NonNull
    private Set<String> serializeEventCodes;

    private Set<String> notifyEventCodes;

    private Set<String> notifyMails;

    /**
     * 离线时间间隔
     */
    private Duration offlineInterval;

    /**
     * 客户端状态
     */
    private ClientStatusEnum status;

    private String token;

    public ClientData(Application application, ClientId clientId, ClientManagerData clientManager) {
        this.id = clientId;
        this.serializeEventCodes = clientManager.getSerializeEventCodes() == null ? new HashSet<>(0) : clientManager.getSerializeEventCodes();
        this.refresh(application, clientManager);
    }

    /**
     * 刷新客户端
     * @param application 应用信息
     * @param clientManager 客户端管理信息
     * @return 刷新后的客户端
     */
    public ClientData refresh(Application application, ClientManagerData clientManager) {
        this.application = application;
        this.statusInterval = clientManager.getStatusInterval();
        this.offlineInterval = clientManager.getOfflineInterval();
        this.token = clientManager.getToken();
        this.refreshTime = Instant.now();
        this.notifyEventCodes = clientManager.getNotifyEventCodes() == null ? new HashSet<>(0) : clientManager.getNotifyEventCodes();
        this.notifyMails = clientManager.getNotifyMails() == null ? new HashSet<>(0) : clientManager.getNotifyMails();
        return this;
    }

}
