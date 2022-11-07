package com.smart.monitor.server.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * 客户端ID
 * @author shizhongming
 * 2021/3/21 10:20 下午
 */
@EqualsAndHashCode
@Getter
@NoArgsConstructor
public final class ClientId implements Serializable, Comparable<ClientId> {
    private static final long serialVersionUID = 517098121163098598L;

    private String value;

    private ClientId(@NonNull String value) {
        this.value = value;
    }

    public static ClientId create(@NonNull String value) {
        return new ClientId(value);
    }


    @Override
    public int compareTo(@NonNull ClientId o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return this.value;
    }

}
