package com.smart.module.api.system.parameter;

import lombok.*;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户部门参数
 * 如果user_id或tenant_id为空，则返回当前用户的部门列表
 * @author shizhongming
 * 2025/3/7 15:25
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUserDeptParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = -5720872442691705119L;
    @Nullable
    private Long userId;
}
