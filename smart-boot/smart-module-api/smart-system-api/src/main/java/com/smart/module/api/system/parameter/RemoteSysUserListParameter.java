package com.smart.module.api.system.parameter;

import lombok.*;

import java.io.Serializable;

/**
 * 查询用户参数
 * @author zhongming4762
 * 2023/7/17 19:35
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoteSysUserListParameter implements Serializable {

    private Boolean useYn;

    private String username;

    private String fullName;

    private String userType;

}
