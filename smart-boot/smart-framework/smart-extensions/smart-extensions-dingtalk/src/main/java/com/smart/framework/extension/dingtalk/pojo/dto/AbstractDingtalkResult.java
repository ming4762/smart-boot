package com.smart.framework.extension.dingtalk.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/4/28 17:16
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode
public abstract class AbstractDingtalkResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 5072531684182100232L;

    @JsonProperty("request_id")
    private String requestId;
}
