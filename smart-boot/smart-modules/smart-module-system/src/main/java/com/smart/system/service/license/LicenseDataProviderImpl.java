package com.smart.system.service.license;

import com.google.common.collect.Lists;
import com.smart.license.core.constants.ConstantsEnum;
import com.smart.license.core.model.LicenseCheckProjectInfo;
import com.smart.license.core.model.LicenseCheckServerInfo;
import com.smart.license.server.LicenseDataProvider;
import com.smart.license.server.LicenseGeneratorParameter;
import com.smart.system.mapper.SmartAuthLicenseMapper;
import com.smart.system.model.SmartAuthLicensePO;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author zhongming4762
 * 2022/12/28 19:51
 */
@Component
public class LicenseDataProviderImpl implements LicenseDataProvider {

    private final SmartAuthLicenseMapper smartAuthLicenseMapper;

    public LicenseDataProviderImpl(SmartAuthLicenseMapper smartAuthLicenseMapper) {
        this.smartAuthLicenseMapper = smartAuthLicenseMapper;
    }

    /**
     * 数据的key
     *
     * @return key
     */
    @Override
    public String key() {
        return null;
    }

    /**
     * 获取返回的数据
     *
     * @param parameter license生成参数
     * @return 数据
     */
    @Override
    public Map<String, Object> dataMap(LicenseGeneratorParameter parameter) {
        SmartAuthLicensePO smartAuthLicense = this.smartAuthLicenseMapper.selectById(parameter.getDataId());
        if (smartAuthLicense == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> data = new HashMap<>(2);
        // 获取服务器信息
        LicenseCheckServerInfo licenseCheckInfo = LicenseCheckServerInfo.builder()
                .macAddressList(this.getStringList(smartAuthLicense.getMacAddress()))
                .ipAddressList(this.getStringList(smartAuthLicense.getIpAddress()))
                .cpuSerial(smartAuthLicense.getCpuSerial())
                .mainBoardSerial(smartAuthLicense.getMainBoardSerial())
                .build();
        data.put(ConstantsEnum.SERVER_INFO.name(), licenseCheckInfo);
        // 获取项目信息
        LicenseCheckProjectInfo projectInfo = LicenseCheckProjectInfo.builder()
                .project(smartAuthLicense.getProject())
                .enterprise(smartAuthLicense.getEnterprise())
                .version(smartAuthLicense.getVersion())
                .build();
        data.put(ConstantsEnum.PROJECT_INFO.name(), projectInfo);
        return data;
    }

    protected List<String> getStringList(String value) {
        return Optional.ofNullable(value)
                .map(item -> Lists.newArrayList(item.split(",")))
                .orElse(new ArrayList<>(0));
    }
}
