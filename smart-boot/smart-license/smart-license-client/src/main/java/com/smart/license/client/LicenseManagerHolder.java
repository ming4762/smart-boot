package com.smart.license.client;

import com.smart.license.core.CustomLicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * @author zhongming4762
 * 2022/12/23
 */
public class LicenseManagerHolder {

    private static volatile CustomLicenseManager LICENSE_MANAGER;

    public static CustomLicenseManager getInstance(LicenseParam param){
        if(LICENSE_MANAGER == null){
            synchronized (LicenseManagerHolder.class){
                if(LICENSE_MANAGER == null){
                    LICENSE_MANAGER = new CustomLicenseManager(param);
                }
            }
        }
        return LICENSE_MANAGER;
    }
}
