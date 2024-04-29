package com.smart.dingtalk.pojo.parameter;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 获取access token参数
 * @author shizhongming
 * 2024/4/26 21:35
 * @since 3.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetAccessTokenParameter implements Serializable {
    @Serial
    private static final long serialVersionUID = -2567069358104376154L;

    private String appKey;

    private String appSecret;
}
