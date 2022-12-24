package com.smart.license.client;

import com.smart.license.client.params.LicenseStoreParam;
import com.smart.license.core.CustomKeyStoreParam;
import com.smart.license.core.CustomLicenseManager;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * @author zhongming4762
 * 2022/12/23
 */
@Slf4j
public class DefaultLicenseVerifier implements LicenseVerifier {
    /**
     * 安装证书
     *
     * @param licenseStoreParam 证书存储参数
     * @return LicenseContent
     */
    @Override
    public LicenseContent install(LicenseStoreParam licenseStoreParam) {
        LicenseContent result = null;
        CustomLicenseManager licenseManager = LicenseManagerHolder.getInstance(this.createLicenseParam(licenseStoreParam));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            licenseManager.uninstall();
            result = licenseManager.install(new File(licenseStoreParam.getLicensePath()));
            log.info("证书安装成功，证书有效期：{}  -  {}", format.format(result.getNotBefore()), format.format(result.getNotAfter()));
        } catch (Exception e) {
            log.error("证书安装失败", e);
        }
        return result;
    }

    /**
     * 验证证书
     *
     * @return 验证结果
     */
    @Override
    public boolean verify() {
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            LicenseContent licenseContent = licenseManager.verify();
            log.info("证书校验通过，证书有效期：{}  -  {}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter()));
            return true;
        } catch (Exception e) {
            log.error("证书验证失败", e);
            return false;
        }
    }

    protected LicenseParam createLicenseParam(LicenseStoreParam licenseStoreParam) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerifier.class);

        CipherParam cipherParam = new DefaultCipherParam(licenseStoreParam.getStorePass());

        CustomKeyStoreParam keyStoreParam = new CustomKeyStoreParam(
                LicenseVerifier.class,
                licenseStoreParam.getPublicKeysStorePath(),
                licenseStoreParam.getPublicAlias(),
                licenseStoreParam.getStorePass(),
                null
        );
        return new DefaultLicenseParam(
                licenseStoreParam.getSubject(),
                preferences,
                keyStoreParam,
                cipherParam
        );
    }
}
