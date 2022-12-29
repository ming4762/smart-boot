package com.smart.license.client.validator;

import com.smart.commons.server.info.ServerInfoProvider;
import com.smart.license.core.LicenseValidator;
import com.smart.license.core.constants.ConstantsEnum;
import com.smart.license.core.model.LicenseCheckServerInfo;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zhongming4762
 * 2022/12/27 20:59
 */
public class ServerInfoValidator implements LicenseValidator {
    /**
     * 验证license
     *
     * @param content LicenseContent
     * @return 是否验证成功
     */
    @Override
    public boolean validate(LicenseContent content) throws LicenseContentException {
        Map<String, Object> extra = (Map<String, Object>) content.getExtra();
        if (extra == null) {
            return true;
        }
        LicenseCheckServerInfo licenseCheckInfo = (LicenseCheckServerInfo) extra.get(ConstantsEnum.SERVER_INFO.name());
        if (licenseCheckInfo == null) {
            return true;
        }
        // 获取当前服务器信息
        LicenseCheckServerInfo serverInfo = this.getServerInfo();
        if (serverInfo == null) {
            throw new LicenseContentException("不能获取服务器硬件信息");
        }
        // 检验mac
        if (!this.checkMacIp(licenseCheckInfo.getMacAddressList(), serverInfo.getMacAddressList())) {
            throw new LicenseContentException("当前服务器的Mac地址不在授权范围内");
        }
        // 校验IP
        if (!this.checkMacIp(licenseCheckInfo.getIpAddressList(), serverInfo.getIpAddressList())) {
            throw new LicenseContentException("当前服务器的IP不在授权范围内");
        }
        // 主板序列号
        if (!this.checkSerial(licenseCheckInfo.getCpuSerial(), serverInfo.getCpuSerial())) {
            throw new LicenseContentException("当前服务器的CPU序列号不在授权范围内");
        }
        if (!this.checkSerial(licenseCheckInfo.getMainBoardSerial(), serverInfo.getMainBoardSerial())) {
            throw new LicenseContentException("当前服务器的主板序列号没在授权范围内");
        }
        return true;
    }

    /**
     * 校验mac地址和IP
     * @param licenseList license允许的列表
     * @param serverList 服务器列表
     * @return 是否通过
     */
    protected boolean checkMacIp(List<String> licenseList, List<String> serverList) {
        if (CollectionUtils.isEmpty(licenseList)) {
            return true;
        }
        if (CollectionUtils.isEmpty(serverList)) {
            return false;
        }
        for (String address : licenseList) {
            if (serverList.contains(address.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证序列号
     * @param licenseSerial 证书中允许的序列号
     * @param serverSerial 服务器序列号
     * @return 是否通过
     */
    protected boolean checkSerial(String licenseSerial, String serverSerial) {
        if (!StringUtils.hasText(licenseSerial)) {
            return true;
        }
        if (!StringUtils.hasText(serverSerial)) {
            return false;
        }
        return licenseSerial.equals(serverSerial);
    }


    /**
     * 获取服务器信息
     * @return 服务器信息
     */
    protected LicenseCheckServerInfo getServerInfo() {
        return LicenseCheckServerInfo.builder()
                .macAddressList(ServerInfoProvider.getMacAddress())
                .ipAddressList(ServerInfoProvider.getIpAddress())
                .cpuSerial(ServerInfoProvider.getCpuSerial())
                .mainBoardSerial(ServerInfoProvider.getMainBoardSerial())
                .build();
    }
}
