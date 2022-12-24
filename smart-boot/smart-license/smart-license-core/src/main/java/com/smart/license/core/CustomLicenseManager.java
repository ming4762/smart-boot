package com.smart.license.core;

import com.smart.commons.server.info.ServerInfoProvider;
import com.smart.license.core.model.LicenseCheckInfo;
import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 自定义LicenseManager
 * @author zhongming4762
 * 2022/12/18 16:20
 */
public class CustomLicenseManager extends LicenseManager {


    public CustomLicenseManager(LicenseParam licenseParam) {
        super(licenseParam);
    }

    /**
     * 重写create函数，自定义校验函数
     * @param content the license content
     *         - may <em>not</em> be {@code null}.
     * @param notary the license notary used to sign the license key
     *         - may <em>not</em> be {@code null}.
     * @return license
     * @throws Exception Exception
     */
    @Override
    protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
        this.initialize(content);
        this.validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }



    protected synchronized void validateCreate(LicenseContent content) throws LicenseContentException {
        Date now = new Date();

        final Date notBefore = content.getNotBefore();
        final Date notAfter = content.getNotAfter();

        if (null != notAfter && now.after(notAfter)){
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)){
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
    }


    /**
     * 重写validate，验证自定义信息
     * @param content the license content
     *         - may <em>not</em> be {@code null}.
     * @throws LicenseContentException LicenseContentException
     */
    @Override
    protected synchronized void validate(LicenseContent content) throws LicenseContentException {
        super.validate(content);

        LicenseCheckInfo licenseCheckInfo = (LicenseCheckInfo) content.getExtra();
        // 获取当前服务器信息
        LicenseCheckInfo serverInfo = this.getServerInfo();
        if (licenseCheckInfo == null || serverInfo == null) {
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
    protected LicenseCheckInfo getServerInfo() {
        return LicenseCheckInfo.builder()
                .macAddressList(ServerInfoProvider.getMacAddress())
                .ipAddressList(ServerInfoProvider.getIpAddress())
                .cpuSerial(ServerInfoProvider.getCpuSerial())
                .mainBoardSerial(ServerInfoProvider.getMainBoardSerial())
                .build();
    }

}
