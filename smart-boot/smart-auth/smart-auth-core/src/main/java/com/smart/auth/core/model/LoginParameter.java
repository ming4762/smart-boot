package com.smart.auth.core.model;

import com.smart.auth.core.constants.LoginTypeEnum;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录参数
 * @author shizhongming
 * 2021/1/1 4:15 上午
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginParameter {

    private String username;

    private String password;

    LoginTypeEnum loginType;

    public static LoginParameter create(@NonNull HttpServletRequest request) {
        String type = request.getParameter("type");
        LoginTypeEnum loginType = LoginTypeEnum.WEB;
        if(StringUtils.isNotBlank(type)) {
            loginType = LoginTypeEnum.valueOf(type);
        }
        return LoginParameter.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .loginType(loginType)
                .build();
    }
}
