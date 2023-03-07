package com.smart.auth.security.pojo.vo;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.constants.LoginTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线用户VO
 * @author zhongming4762
 * 2023/3/7
 */
@Getter
@Setter
@ToString
public class OnlineUserVO implements Serializable {


    private static final long serialVersionUID = 3127805743664997934L;
    private Long userId;

    private String username;

    private String fullName;

    private List<UserLoginData> userLoginDataList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLoginData implements Serializable {
        private static final long serialVersionUID = 187324682045393739L;
        private String loginIp;

        /**
         * 认证类型
         */
        private AuthTypeEnum authType;

        /**
         * 登录类型
         */
        private LoginTypeEnum loginType;

        /**
         * 登录时间
         */
        private LocalDateTime loginTime;

        /**
         * 是否绑定IP
         */
        private Boolean bindIp;

        private String token;

        private Duration timeout;
    }
}
