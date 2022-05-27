package com.smart.system.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ShiZhongMing
 * 2021/6/30 10:51
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class ChangePasswordDTO {

    @NotNull(message = "原密码不能为空")
    private String oldPassword;

    @NotNull(message = "新密码不能为空")
    private String newPassword;

    @NotNull(message = "确认新密码不能为空")
    private String newPasswordConfirm;
}
