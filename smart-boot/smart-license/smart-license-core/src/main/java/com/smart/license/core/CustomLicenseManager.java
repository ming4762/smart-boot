package com.smart.license.core;

import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 自定义LicenseManager
 * @author zhongming4762
 * 2022/12/18 16:20
 */
public class CustomLicenseManager extends LicenseManager {

    private final List<LicenseValidator> licenseValidatorList;

    public CustomLicenseManager(LicenseParam licenseParam, List<LicenseValidator> licenseValidatorList) {
        super(licenseParam);
        this.licenseValidatorList = licenseValidatorList;
    }

    /**
     * 重写create函数，自定义校验函数
     * @param content the license content
     *         - may <em>not</em> be {@code null}.
     * @param notary the license notary used to sign the license key
     *         - may <em>not</em> be {@code null}.
     * @return license
     * @throws Exception Exception
     */
    @Override
    protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
        this.initialize(content);
        this.validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }



    protected synchronized void validateCreate(LicenseContent content) throws LicenseContentException {
        Date now = new Date();

        final Date notBefore = content.getNotBefore();
        final Date notAfter = content.getNotAfter();

        if (null != notAfter && now.after(notAfter)){
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)){
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
    }


    /**
     * 重写validate，验证自定义信息
     * @param content the license content
     *         - may <em>not</em> be {@code null}.
     * @throws LicenseContentException LicenseContentException
     */
    @Override
    protected synchronized void validate(LicenseContent content) throws LicenseContentException {
        super.validate(content);
        if (!CollectionUtils.isEmpty(this.licenseValidatorList)) {
            for (LicenseValidator validator : this.licenseValidatorList) {
                boolean result = validator.validate(content);
                if (!result) {
                    break;
                }
            }
        }
    }
}
