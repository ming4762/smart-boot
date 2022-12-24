package com.smart.license.client.params;

/**
 * @author shizhongming
 * 2022/12/4 18:14
 */
public interface LicenseStoreParamProvider {

    /**
     * 获取license存储参数
     * @return LicenseStoreParam
     */
    LicenseStoreParam get();
}
