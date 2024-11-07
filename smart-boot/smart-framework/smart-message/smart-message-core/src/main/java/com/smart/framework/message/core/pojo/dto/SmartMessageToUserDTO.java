package com.smart.framework.message.core.pojo.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息接收的用户信息
 * @author shizhongming
 * 2024/5/20 20:29
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SmartMessageToUserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4595626005650181298L;

    private Long userId;

    private String username;

    private String fullName;

    private String mobile;

    private String email;
}
