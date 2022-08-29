package com.smart.auth.core.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * 临时token缓存数据
 * @author ShiZhongMing
 * 2021/3/9 15:14
 * @since 1.0
 */
@Getter
@Setter
public class TempTokenData implements Serializable {
    @Serial
    private static final long serialVersionUID = -9173549970442451354L;

    /**
     * 申请用户ID
     */
    private String ip;

    /**
     * 申请用户ID
     */
    private Long userId;

    /**
     * 申请的资源
     */
    private String resource;

    /**
     * 是否只使用一次
     */
    private Boolean once = Boolean.TRUE;
}
