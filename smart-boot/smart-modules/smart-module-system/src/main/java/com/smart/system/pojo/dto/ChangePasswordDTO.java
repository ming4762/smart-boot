package com.smart.system.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2021/6/30 10:51
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class ChangePasswordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7468662627637387665L;
    @NotNull(message = "原密码不能为空")
    private String oldPassword;

    @NotNull(message = "新密码不能为空")
    private String newPassword;

    @NotNull(message = "确认新密码不能为空")
    private String newPasswordConfirm;
}
