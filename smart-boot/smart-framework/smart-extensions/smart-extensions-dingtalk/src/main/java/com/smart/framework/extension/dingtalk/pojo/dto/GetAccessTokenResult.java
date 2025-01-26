package com.smart.framework.extension.dingtalk.pojo.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author shizhongming
 * 2024/4/26 21:40
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetAccessTokenResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -952504222553271390L;

    private String accessToken;

    private ZonedDateTime expireAt;

}
