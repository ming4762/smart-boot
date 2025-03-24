package com.smart.framework.message.dingtalk;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/5/20 20:55
 * @since 3.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SmartMessageDingtalkChannelProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = -2614821682130427021L;
    private String appKey;

    private String appSecret;

    private Long agentId;
}
