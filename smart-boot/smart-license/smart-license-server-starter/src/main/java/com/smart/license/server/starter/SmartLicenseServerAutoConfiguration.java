package com.smart.license.server.starter;

import com.smart.license.server.DefaultLicenseGenerator;
import com.smart.license.server.LicenseDataProvider;
import com.smart.license.server.LicenseGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * License server自动配置类
 * @author zhongming4762
 * 2022/12/22
 */
@Configuration
@ConditionalOnClass(LicenseGenerator.class)
public class SmartLicenseServerAutoConfiguration {

    /**
     * license生成器
     * @return license 生成器
     */
    @Bean
    @ConditionalOnMissingBean(LicenseGenerator.class)
    public LicenseGenerator licenseGenerator(List<LicenseDataProvider> dataProviderList) {
        return new DefaultLicenseGenerator(dataProviderList);
    }
}
