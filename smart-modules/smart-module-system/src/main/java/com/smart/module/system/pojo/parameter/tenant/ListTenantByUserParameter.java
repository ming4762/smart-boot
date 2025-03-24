package com.smart.module.system.pojo.parameter.tenant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author shizhongming
 * 2024/4/16 8:53
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class ListTenantByUserParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = 6505162013107164172L;

    public ListTenantByUserParameter(Long userId) {
        this.userId = userId;
        this.now = ZonedDateTime.now();
    }

    private Long userId;

    private ZonedDateTime now;
}
