package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysUserApi;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.module.api.system.dto.*;
import com.smart.module.api.system.parameter.RemoteSysUserListParameter;
import com.smart.module.api.system.parameter.SysUserDeptParameter;
import com.smart.module.api.system.parameter.SysUserThirdAccountParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shizhongming
 * 2025/6/15 20:52
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSysUserApiFallback implements FallbackFactory<RemoteSysUserApi> {

    @Override
    public RemoteSysUserApi create(Throwable cause) {
        return new RemoteSysUserApi() {

            private void errorLog() {
                log.error("RemoteSysUserApiFallback", cause);
            }
            @Override
            public List<SysUserDTO> listUserByUsername(List<String> usernameList) {
                this.errorLog();
                throw new SystemException(cause);
            }

            @Override
            public List<SysUserDTO> listUserById(List<Long> userIdList) {
                this.errorLog();
                return List.of();
            }

            @Override
            public boolean lockAccount(UserAccountLockDTO parameter) {
                this.errorLog();
                return false;
            }

            @Override
            public boolean updateLoginFailTime(AccountLoginFailTimeUpdateDTO userId) {
                this.errorLog();
                return false;
            }

            @Override
            public List<SysUserDTO> listUser(RemoteSysUserListParameter parameter) {
                this.errorLog();
                return List.of();
            }

            @Override
            public List<SysDeptDTO> listUserDept(SysUserDeptParameter parameter) {
                this.errorLog();
                return List.of();
            }

            @Override
            public List<SysDeptDTO> listUserDeptWithChildren(SysUserDeptParameter parameter) {
                this.errorLog();
                return List.of();
            }

            /**
             * 查询用户第三方账号列表
             *
             * @param parameter 参数
             * @return 用户第三方账号列表
             */
            @Override
            public List<SysUserThirdAccountDTO> listUserThirdAccount(SysUserThirdAccountParameter parameter) {
                throw new SystemException("查询用户第三方账号列表失败");
            }
        };
    }
}
