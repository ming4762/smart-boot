package com.smart.license.client;

import com.smart.license.core.CustomLicenseManager;
import com.smart.license.core.LicenseValidator;
import de.schlichtherle.license.LicenseParam;

import java.util.List;

/**
 * @author zhongming4762
 * 2022/12/23
 */
public class LicenseManagerHolder {

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
