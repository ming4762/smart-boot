package com.smart.license.server;

import com.smart.license.core.CustomKeyStoreParam;
import com.smart.license.core.CustomLicenseManager;
import de.schlichtherle.license.*;
import lombok.SneakyThrows;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * @author zhongming4762
 * 2022/12/18
 */
public class DefaultLicenseGenerator implements LicenseGenerator {

    private static final X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN");

    private List<LicenseDataProvider> dataProviderList;

    public DefaultLicenseGenerator(List<LicenseDataProvider> dataProviderList) {
        this.dataProviderList = dataProviderList;
    }

    /**
     * 生成License
     *
     * @param parameter license生成参数
     * @return license
     */
    @SneakyThrows
    @Override
    public boolean generate(LicenseGeneratorParameter parameter) {
        LicenseManager licenseManager = new CustomLicenseManager(this.createLicenseParam(parameter), null);
        LicenseContent licenseContent = this.createContent(parameter);

        licenseManager.store(licenseContent, new File(parameter.getLicensePath()));
        return true;
    }

    protected LicenseParam createLicenseParam(LicenseGeneratorParameter parameter) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseGenerator.class);
        // 密码为null
        CipherParam cipherParam = new DefaultCipherParam(parameter.getStorePassword());

        CustomKeyStoreParam keyStoreParam = new CustomKeyStoreParam(
                LicenseGenerator.class,
                parameter.getStorePath(),
                parameter.getAlias(),
                parameter.getStorePassword(),
                parameter.getKeyPassword()
        );

        return new DefaultLicenseParam(
                parameter.getSubject(),
                preferences,
                keyStoreParam,
                cipherParam
        );
    }

    /**
     * 创建证书生成参数
     * @param parameter 参数
     * @return LicenseContent
     */
    protected LicenseContent createContent(LicenseGeneratorParameter parameter) {
        LicenseContent content = new LicenseContent();

        content.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        content.setIssuer(DEFAULT_HOLDER_AND_ISSUER);

        content.setSubject(parameter.getSubject());
        content.setIssued(Date.from(parameter.getIssuedTime().atZone(ZoneId.systemDefault()).toInstant()));
        content.setNotBefore(Date.from(parameter.getIssuedTime().atZone(ZoneId.systemDefault()).toInstant()));
        content.setNotAfter(Date.from(parameter.getExpiryTime().atZone(ZoneId.systemDefault()).toInstant()));

        // 设置用户数
        if (parameter.getConsumerAmount() != null && parameter.getConsumerAmount() > 0) {
            content.setConsumerAmount(parameter.getConsumerAmount().intValue());
        }
        content.setInfo(parameter.getDescription());
        // 设置额外需要校验的信息
        Map<String, Object> extraMap = new HashMap<>(8);
        this.dataProviderList.forEach(item -> {
            Map<String, Object> map = item.dataMap(parameter);
            if (map != null) {
                extraMap.putAll(map);
            }
        });
        content.setExtra(extraMap);

        return content;
    }
}
