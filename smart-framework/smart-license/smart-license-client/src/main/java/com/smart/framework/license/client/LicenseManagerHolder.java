package com.smart.framework.license.client;

import com.smart.framework.license.core.CustomLicenseManager;
import com.smart.framework.license.core.LicenseValidator;
import de.schlichtherle.license.LicenseParam;

import java.util.List;

/**
 * @author zhongming4762
 * 2022/12/23
 */
public class LicenseManagerHolder {

    private LicenseManagerHolder() {
        throw new IllegalStateException("Utility class");
    }

    private static volatile CustomLicenseManager LICENSE_MANAGER;

    public static CustomLicenseManager getInstance(LicenseParam param, List<LicenseValidator> licenseValidatorList){
        if(LICENSE_MANAGER == null){
            synchronized (LicenseManagerHolder.class){
                if(LICENSE_MANAGER == null){
                    LICENSE_MANAGER = new CustomLicenseManager(param, licenseValidatorList);
                }
            }
        }
        return LICENSE_MANAGER;
    }
}
