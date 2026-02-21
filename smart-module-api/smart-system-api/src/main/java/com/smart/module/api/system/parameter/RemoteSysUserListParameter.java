package com.smart.module.api.system.parameter;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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

    @Serial
    private static final long serialVersionUID = -936208086924410677L;
    
    private Boolean useYn;

    private String username;

    private String fullName;

    private List<Long> userIdList;
}
