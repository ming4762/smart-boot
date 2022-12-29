package com.smart.license.client.params;

import com.smart.license.core.model.LicenseCheckProjectInfo;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author zhongming4762
 * 2022/12/28 20:26
 */
public class PropertiesProjectInfoProvider implements ProjectInfoProvider {

    /**
     * 企业
     */
    @Value("${smart.license.client.project.enterprise:null}")
    private String enterprise;

    /**
     * 项目&系统
     */
    @Value("${smart.license.client.project.project:null}")
    private String project;

    /**
     * 系统版本号
     */
    @Value("${smart.license.client.project.version:null}")
    private String version;

    /**
     * 获取项目信息
     *
     * @return 项目信息
     */
    @Override
    public LicenseCheckProjectInfo projectInfo() {
        return LicenseCheckProjectInfo.builder()
                .enterprise(this.enterprise)
                .project(this.project)
                .version(this.version)
                .build();
    }
}
