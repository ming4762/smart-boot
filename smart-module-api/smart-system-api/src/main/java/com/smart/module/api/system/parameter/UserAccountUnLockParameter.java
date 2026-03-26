package com.smart.module.api.system.parameter;

import com.smart.framework.commons.core.dto.auth.UserAccountStatusEnum;
import lombok.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户账户解锁参数
 * @author zhongming4762
 * 2023/6/7
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountUnLockParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = -1268955707784009659L;
    @NonNull
    private Long userId;

    /**
     * 账户状态
     * 不为空，只有该状态的账户才会解锁
     * 空 锁定状态的账户都会解锁
     */
    @Nullable
    private UserAccountStatusEnum accountStatus;
}
