package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysUserApi;
import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import com.smart.module.api.system.dto.SysDeptDTO;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.api.system.dto.UserAccountLockDTO;
import com.smart.module.api.system.parameter.RemoteSysUserListParameter;
import com.smart.module.api.system.parameter.SysUserDeptParameter;
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
                return List.of();
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
        };
    }
}
