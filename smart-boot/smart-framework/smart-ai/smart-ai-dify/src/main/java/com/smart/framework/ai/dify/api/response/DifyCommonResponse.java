package com.smart.framework.ai.dify.api.response;

import lombok.Getter;
import lombok.Setter;

/**
 * dify 通用响应
 * @author shizhongming
 * 2025/2/10 20:26
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyCommonResponse {

    private static final String SUCCESS = "success";

    private String result;

    public boolean isSuccess() {
        return SUCCESS.equals(result);
    }
}
