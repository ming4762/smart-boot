package com.smart.license.client.params;

import com.smart.license.core.model.LicenseCheckProjectInfo;

/**
 * @author zhongming4762
 * 2022/12/28 20:24
 */
public interface ProjectInfoProvider {

    /**
     * 获取项目信息
     * @return 项目信息
     */
    LicenseCheckProjectInfo projectInfo();
}
