package com.smart.system.pojo.vo;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.system.model.SysUserPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线用户VO
 * @author ShiZhongMing
 * 2022/1/17
 * @since 2.0.0
 */
@ToString
@Getter
@Setter
public class SysOnlineUserVO extends SysUserPO {


    private static final long serialVersionUID = 3127805743664997934L;
    private List<UserLoginData> userLoginDataList;

    @Getter
    @Setter
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
    }
}
