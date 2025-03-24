package com.smart.framework.ai.dify.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 应用基本信息
 * @author shizhongming
 * 2025/2/10 19:27
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyInfoResponse {

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 应用标签
     */
    private List<String> tags;
}
