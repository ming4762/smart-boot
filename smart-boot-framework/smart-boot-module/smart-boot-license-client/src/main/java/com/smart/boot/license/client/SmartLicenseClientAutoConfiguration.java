package com.smart.boot.license.client;

import com.smart.framework.license.client.DefaultLicenseVerifier;
import com.smart.framework.license.client.LicenseInstallListener;
import com.smart.framework.license.client.LicenseVerifier;
import com.smart.framework.license.client.params.LicenseStoreParamProvider;
import com.smart.framework.license.client.params.PropertiesLicenseStoreParamProvider;
import com.smart.framework.license.core.LicenseValidator;
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
@ConditionalOnClass(LicenseVerifier.class)
public class SmartLicenseClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(LicenseVerifier.class)
    public LicenseVerifier licenseVerifier(List<LicenseValidator> licenseValidatorList) {
        return new DefaultLicenseVerifier(licenseValidatorList);
    }

    @Bean
    @ConditionalOnMissingBean(LicenseStoreParamProvider.class)
    public LicenseStoreParamProvider licenseStoreParamProvider() {
        return new PropertiesLicenseStoreParamProvider();
    }


    @Bean
    public LicenseInstallListener licenseInstallListener(LicenseStoreParamProvider paramProvider, LicenseVerifier licenseVerifier) {
        return new LicenseInstallListener(paramProvider, licenseVerifier);
    }


}
