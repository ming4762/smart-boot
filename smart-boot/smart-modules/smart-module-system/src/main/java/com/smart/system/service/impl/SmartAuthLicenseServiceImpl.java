package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.license.server.LicenseGenerator;
import com.smart.license.server.LicenseGeneratorParameter;
import com.smart.system.constants.LicenseStatusEnum;
import com.smart.system.mapper.SmartAuthLicenseMapper;
import com.smart.system.model.SmartAuthLicensePO;
import com.smart.system.service.SmartAuthLicenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* smart_auth_license - 许可证管理 Service实现类
* @author SmartCodeGenerator
* 2022-12-17 14:31:50
*/
@Service
public class SmartAuthLicenseServiceImpl extends BaseServiceImpl<SmartAuthLicenseMapper, SmartAuthLicensePO> implements SmartAuthLicenseService {

    private final LicenseGenerator licenseGenerator;

    public SmartAuthLicenseServiceImpl(LicenseGenerator licenseGenerator) {
        this.licenseGenerator = licenseGenerator;
    }

    /**
     * 生成license
     *
     * @param id id
     * @return 是否生成成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generator(Long id) {
        // 查询数据
        SmartAuthLicensePO data = this.getById(id);
        if (data == null) {
            return false;
        }
        // 生成license
        LicenseGeneratorParameter parameter = LicenseGeneratorParameter.builder()
                .subject(data.getSubject())
                .storePath(data.getStorePath())
                .storePassword(data.getStorePassword())
                .keyPassword(data.getKeyPassword())
                .alias(data.getAlias())
                .licensePath(data.getLicensePath())
                .issuedTime(data.getEffectiveTime())
                .expiryTime(data.getExpirationTime())
                .consumerAmount(data.getUserNum())
                .description(data.getRemark())
                .dataId(id)
                .build();
        this.licenseGenerator.generate(parameter);
        return this.update(
                new UpdateWrapper<SmartAuthLicensePO>().lambda()
                        .eq(SmartAuthLicensePO::getId, data.getId())
                        .set(SmartAuthLicensePO::getStatus, LicenseStatusEnum.GENERATOR)
        );
    }
}
