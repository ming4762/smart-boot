package com.smart.autoconfigure.license.client;

import com.smart.license.client.params.ProjectInfoProvider;
import com.smart.license.client.params.PropertiesProjectInfoProvider;
import com.smart.license.client.validator.ProjectInfoValidator;
import com.smart.license.client.validator.ServerInfoValidator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2022/12/28 20:44
 */
@Configuration
@AutoConfigureBefore(SmartLicenseClientAutoConfiguration.class)
public class SmartLicenseValidatorAutoConfiguration {

    /**
     * 服务器信息校验类
     * @return ServerInfoValidator
     */
    @Bean
    @ConditionalOnMissingBean(ServerInfoValidator.class)
    public ServerInfoValidator serverInfoValidator() {
        return new ServerInfoValidator();
    }

    /**
     * 提供项目信息
     * @return ProjectInfoProvider
     */
    @Bean
    @ConditionalOnMissingBean(ProjectInfoProvider.class)
    public ProjectInfoProvider projectInfoProvider() {
        return new PropertiesProjectInfoProvider();
    }

    /**
     * 项目信息校验类
     * @param projectInfoProvider ProjectInfoProvider
     * @return ProjectInfoValidator
     */
    @Bean
    @ConditionalOnMissingBean(ProjectInfoValidator.class)
    public ProjectInfoValidator projectInfoValidator(ProjectInfoProvider projectInfoProvider) {
        return new ProjectInfoValidator(projectInfoProvider);
    }
}
