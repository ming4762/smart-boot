package com.smart.license.client;

import com.smart.license.client.params.LicenseStoreParam;
import de.schlichtherle.license.LicenseContent;

/**
 * license验证类
 * @author zhongming4762
 * 2022/12/23
 */
public interface LicenseVerifier {

    /**
     * 安装证书
     * @param licenseStoreParam 证书存储参数
     * @return LicenseContent
     */
    LicenseContent install(LicenseStoreParam licenseStoreParam);

    /**
     * 验证证书
     * @return 验证结果
     */
    boolean verify();
}
