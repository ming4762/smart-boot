package com.smart.license.client.validator;

import com.smart.license.client.params.ProjectInfoProvider;
import com.smart.license.core.LicenseValidator;
import com.smart.license.core.constants.ConstantsEnum;
import com.smart.license.core.model.LicenseCheckProjectInfo;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 验证项目信息
 * @author zhongming4762
 * 2022/12/28 20:21
 */
public class ProjectInfoValidator implements LicenseValidator {

    private final ProjectInfoProvider projectInfoProvider;

    public ProjectInfoValidator(ProjectInfoProvider projectInfoProvider) {
        this.projectInfoProvider = projectInfoProvider;
    }

    /**
     * 验证license
     *
     * @param content LicenseContent
     * @return 是否验证成功
     * @throws LicenseContentException LicenseContentException
     */
    @Override
    public boolean validate(LicenseContent content) throws LicenseContentException {
        Map<String, Object> extra = (Map<String, Object>) content.getExtra();
        if (extra == null) {
            return true;
        }
        LicenseCheckProjectInfo licenseProjectInfo = (LicenseCheckProjectInfo) extra.get(ConstantsEnum.PROJECT_INFO.name());
        if (licenseProjectInfo == null) {
            return true;
        }
        LicenseCheckProjectInfo projectInfo = this.projectInfoProvider.projectInfo();
        boolean enterpriseResult = this.validateField(licenseProjectInfo.getEnterprise(), projectInfo, LicenseCheckProjectInfo::getEnterprise);
        if (!enterpriseResult) {
            throw new LicenseContentException("企业信息校验失败");
        }
        if (!this.validateField(licenseProjectInfo.getProject(), projectInfo, LicenseCheckProjectInfo::getProject)) {
            throw new LicenseContentException("项目信息校验失败");
        }
        if (!this.validateField(licenseProjectInfo.getVersion(), projectInfo, LicenseCheckProjectInfo::getVersion)) {
            throw new LicenseContentException("系统版本信息校验失败");
        }
        return true;
    }

    protected boolean validateField(String licenseField, LicenseCheckProjectInfo projectInfo, Function<LicenseCheckProjectInfo, String> getHandler) {
        if (!StringUtils.hasText(licenseField)) {
            return true;
        }
        String projectField = Optional.ofNullable(projectInfo)
                .map(getHandler)
                .orElse(null);
        return licenseField.equals(projectField);
    }
}
