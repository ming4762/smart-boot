package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysTenantApi;
import com.smart.module.api.system.dto.SysTenantDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shizhongming
 * 2025/6/15 20:50
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSysTenantApiFallback implements FallbackFactory<RemoteSysTenantApi> {

    @Override
    public RemoteSysTenantApi create(Throwable cause) {
        return new RemoteSysTenantApi() {

            private void errorLog() {
                log.error("RemoteSysTenantApiFallback", cause);
            }
            @Override
            public List<SysTenantDTO> listTenantById(List<Long> tenantIdList) {
                this.errorLog();
                return List.of();
            }

            @Override
            public List<SysTenantDTO> listTenantByCode(List<String> tenantCodeList) {
                this.errorLog();
                return List.of();
            }
        };
    }
}
