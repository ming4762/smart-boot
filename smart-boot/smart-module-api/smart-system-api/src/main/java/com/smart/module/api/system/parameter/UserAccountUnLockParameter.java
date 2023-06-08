package com.smart.module.api.system.parameter;

import com.smart.module.api.system.constants.UserAccountStatusEnum;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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

    @NonNull
    private Long userId;

    /**
     * 账户状态
     * 不为空，只有改状态的账户才会结果
     * 空 锁定状态的账户都会结果
     */
    @Nullable
    private UserAccountStatusEnum accountStatus;
}
