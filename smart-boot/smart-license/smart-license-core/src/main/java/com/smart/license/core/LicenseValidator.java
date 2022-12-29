package com.smart.license.core;

import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;

/**
 * license验证器
 * @author zhongming4762
 * 2022/12/27 20:58
 */
public interface LicenseValidator {

    /**
     * 验证license
     * @param content LicenseContent
     * @return 是否验证成功
     * @throws LicenseContentException LicenseContentException
     */
    boolean validate(LicenseContent content) throws LicenseContentException;
}
