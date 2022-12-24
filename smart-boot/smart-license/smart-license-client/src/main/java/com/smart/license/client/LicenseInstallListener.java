package com.smart.license.client;

import com.smart.license.client.params.LicenseStoreParamProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

/**
 * @author zhongming4762
 * 2022/12/23
 */
@Slf4j
public class LicenseInstallListener implements ApplicationListener<ContextRefreshedEvent> {

    private final LicenseStoreParamProvider licenseStoreParamProvider;

    private final LicenseVerifier licenseVerifier;

    public LicenseInstallListener(LicenseStoreParamProvider licenseStoreParamProvider, LicenseVerifier licenseVerifier) {
        this.licenseVerifier = licenseVerifier;
        this.licenseStoreParamProvider = licenseStoreParamProvider;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext().getParent();
        if (applicationContext == null) {
            log.info("开始安装证书");
            this.licenseVerifier.install(this.licenseStoreParamProvider.get());
        }
    }
}
