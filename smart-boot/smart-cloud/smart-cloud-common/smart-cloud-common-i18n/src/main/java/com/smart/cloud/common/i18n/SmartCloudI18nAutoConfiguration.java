package com.smart.cloud.common.i18n;

import com.smart.cloud.common.i18n.reader.RemoteResourceReader;
import com.smart.i18n.SmartI18nAutoConfiguration;
import com.smart.module.api.system.SysI18nApi;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SmartI18nAutoConfiguration.class)
public class SmartCloudI18nAutoConfiguration {

    @Bean
    public RemoteResourceReader remoteResourceReader(SysI18nApi sysI18nApi) {
        return new RemoteResourceReader(sysI18nApi);
    }
}
