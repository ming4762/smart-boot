package com.smart.auth.core.model;

import com.smart.commons.core.http.HttpMethod;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限信息
 * @author shizhongming
 * 2020/4/29 10:55 上午
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = -8062914682511384643L;
    /**
     * 请求方法
     */
    private HttpMethod method;

    private String url;

    private String authority;
}
