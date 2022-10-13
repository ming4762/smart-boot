package com.smart.system.pojo.dto.user;

import com.smart.commons.validate.constraints.Mobile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2020/6/2 4:48 下午
 */
@Getter
@Setter
@ToString
public class UserSaveUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8790839450326558559L;


    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    private String fullName;


    /**
     * 邮箱
     */
    @Email(message = "email格式错误")
    private String email;

    /**
     * 手机
     */
    @Mobile(message = "手机号码格式错误")
    private String mobile;


    /**
     * 用户类型（10：系统用户，20：业务用户）
     */
    @NotNull(message = "用户类型不能为空")
    private String userType;



    /**
     * 序号
     */
    private Integer seq;
}
