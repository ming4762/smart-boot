package com.smart.license.client.params;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author shizhongming
 * 2022/12/4 18:15
 */
public class PropertiesLicenseStoreParamProvider implements LicenseStoreParamProvider {

    @Value("${smart.license.client.subject:null}")
    private String subject;

    @Value("${smart.license.client.publicAlias:null}")
    private String publicAlias;

    @Value("${smart.license.client.storePass:null}")
    private String storePass;

    @Value("${smart.license.client.licensePath:null}")
    private String licensePath;

    @Value("${smart.license.client.publicKeysStorePath:null}")
    private String publicKeysStorePath;

    /**
     * 获取license存储参数
     *
     * @return LicenseStoreParam
     */
    @Override
    public LicenseStoreParam get() {
        return LicenseStoreParam.builder()
                .subject(this.subject)
                .publicAlias(this.publicAlias)
                .storePass(this.storePass)
                .licensePath(this.licensePath)
                .publicKeysStorePath(this.publicKeysStorePath)
                .build();
    }
}
