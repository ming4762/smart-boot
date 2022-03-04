package com.smart.monitor.core.model;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 注册数据
 * @author shizhongming
 * 2021/3/18 8:52 下午
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application implements Serializable {

    private static final long serialVersionUID = 2847498878134595865L;
    /**
     * 所有端点
     */
    @Builder.Default
    private Set<String> endPoints = new HashSet<>();

    private String applicationName;

    /**
     * 端点地址
     */
    private String managementUrl;

    /**
     * 客户端服务地址
     */
    private String clientUrl;

    /**
     * 元数据
     */
    private Map<String, String> metadata;

    /**
     * 服务端地址
     */
    private String serverUrl;

    /**
     * 启动事件
     */
    private long startupTime;

    public boolean hasEndPoint(String endPoint) {
        return this.endPoints.contains(endPoint);
    }

    /**
     * 获取端点地址
     * @param endPoint 端点
     * @return 端点地址
     */
    public String getEndPointUrl(String endPoint) {
        return String.format("%s/%s", this.managementUrl, endPoint);
    }
}
