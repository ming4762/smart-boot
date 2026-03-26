package com.smart.module.api.system.dto;

import lombok.*;
import org.jspecify.annotations.NonNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/4/8 22:24
 * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QueryUserAccountDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7466814291393270836L;

    @NonNull
    private Long userId;

    private Long tenantId;
}
