package com.smart.system.service.auth.impl;

import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.commons.core.exception.BusinessException;
import com.smart.commons.core.exception.SystemException;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.file.core.parameter.FileSaveParameter;
import com.smart.file.core.pojo.bo.FileDownloadResult;
import com.smart.file.core.pojo.bo.FileHandlerResult;
import com.smart.file.core.service.FileService;
import com.smart.license.server.LicenseGenerator;
import com.smart.license.server.LicenseGeneratorParameter;
import com.smart.system.constants.LicenseStatusEnum;
import com.smart.system.mapper.auth.SmartAuthLicenseMapper;
import com.smart.system.model.auth.SmartAuthLicensePO;
import com.smart.system.model.auth.SmartAuthSecretKeyPO;
import com.smart.system.pojo.vo.license.SmartAuthLicenseQueryVO;
import com.smart.system.service.SysSystemService;
import com.smart.system.service.auth.SmartAuthLicenseService;
import com.smart.system.service.auth.SmartAuthSecretKeyService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
* smart_auth_license - 许可证管理 Service实现类
* @author SmartCodeGenerator
* 2022-12-17 14:31:50
*/
@Service
public class SmartAuthLicenseServiceImpl extends BaseServiceImpl<SmartAuthLicenseMapper, SmartAuthLicensePO> implements SmartAuthLicenseService {

    private static final String FILE_TYPE_SECRECY = "SECRECY";

    private final LicenseGenerator licenseGenerator;

    private final SysSystemService systemService;

    private final SmartAuthSecretKeyService smartAuthSecretKeyService;

    private final FileService fileService;

    public SmartAuthLicenseServiceImpl(@Lazy LicenseGenerator licenseGenerator, SysSystemService systemService, SmartAuthSecretKeyService smartAuthSecretKeyService, FileService fileService) {
        this.licenseGenerator = licenseGenerator;
        this.systemService = systemService;
        this.smartAuthSecretKeyService = smartAuthSecretKeyService;
        this.fileService = fileService;
    }

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    @Override
    public SmartAuthLicenseQueryVO getById(Serializable id) {
        SmartAuthLicensePO license = super.getById(id);
        SmartAuthLicenseQueryVO vo = new SmartAuthLicenseQueryVO();
        BeanUtils.copyProperties(license, vo);
        if (vo.getSystemId() != null) {
            vo.setSystem(this.systemService.getById(vo.getSystemId()));
        }
        return vo;
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
        // 查询秘钥信息
        SmartAuthSecretKeyPO smartAuthSecretKey = this.getSecretKey(data.getSecretKeyId());
        FileDownloadResult privateKeyData = this.fileService.download(smartAuthSecretKey.getPrivateKeyFileId());
        if (privateKeyData == null) {
            throw new SystemException("获取秘钥文件失败");
        }
        // 生成license
        LicenseGeneratorParameter parameter = LicenseGeneratorParameter.builder()
                .subject(data.getSubject())
                .privateKeyInputStream(privateKeyData.getInputStream())
                .storePassword(smartAuthSecretKey.getStorePassword())
                .keyPassword(smartAuthSecretKey.getKeyPassword())
                .alias(smartAuthSecretKey.getAlias())
                .issuedTime(data.getEffectiveTime())
                .expiryTime(data.getExpirationTime())
                .consumerAmount(data.getUserNum())
                .description(data.getRemark())
                .dataId(id)
                .build();
        InputStream inputStream = this.licenseGenerator.generate(parameter);
        // 保存license文件
        FileHandlerResult licenseFile = this.fileService.save(
                inputStream,
                FileSaveParameter.builder()
                        .type(FILE_TYPE_SECRECY)
                        .fileStorageId(data.getFileStorageId())
                        .filename(data.getLicenseName() + ".lic")
                        .build()
        );
        return this.update(
                new UpdateWrapper<SmartAuthLicensePO>().lambda()
                        .eq(SmartAuthLicensePO::getId, data.getId())
                        .set(SmartAuthLicensePO::getLicenseFileId, licenseFile.getFileId())
                        .set(SmartAuthLicensePO::getStatus, LicenseStatusEnum.GENERATOR)
        );
    }

    /**
     * 下载license
     *
     * @param id           license id
     * @param outputStream 输出流
     */
    @Override
    public void download(Serializable id, OutputStream outputStream) {
        // 查询license信息
        SmartAuthLicensePO authLicense = this.baseMapper.selectById(id);
        if (authLicense == null) {
            throw new BusinessException("获取license失败，请检查ID是否正确");
        }
        if (!Boolean.TRUE.equals(authLicense.getUseYn())) {
            throw new BusinessException("license已停用");
        }
        if (!LicenseStatusEnum.GENERATOR.equals(authLicense.getStatus())) {
            throw new BusinessException("license未创建");
        }
        // 获取秘钥信息
        SmartAuthSecretKeyPO authSecretKey = this.smartAuthSecretKeyService.getById(authLicense.getSecretKeyId());
        if (authSecretKey == null) {
            throw new BusinessException("获取秘钥信息失败");
        }
        // 获取公钥
        FileDownloadResult publicKeyFile = this.fileService.download(authSecretKey.getPublicKeyFileId());
        FileDownloadResult licenseFile = this.fileService.download(authLicense.getLicenseFileId());
        if (publicKeyFile == null || licenseFile == null) {
            throw new BusinessException("获取文件失败，请检查文件是否已删除");
        }
        ZipUtil.zip(outputStream, new String[]{publicKeyFile.getFileName(), licenseFile.getFileName()}, new InputStream[]{publicKeyFile.getInputStream(), licenseFile.getInputStream()});
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<SmartAuthLicensePO> entityList) {
        Set<Long> deleteFileIds = new HashSet<>();
        entityList.forEach(item -> {
            if (item.getId() != null && LicenseStatusEnum.GENERATOR.equals(item.getStatus())) {
                item.setStatus(LicenseStatusEnum.UPDATE);
                deleteFileIds.add(item.getId());
                item.setLicenseFileId(null);
            }
        });
        boolean result = super.saveOrUpdateBatch(entityList);
        if (!deleteFileIds.isEmpty()) {
            // 获取文件ID
            Set<Long> fileIds = this.list(
                    new QueryWrapper<SmartAuthLicensePO>().lambda()
                            .select(SmartAuthLicensePO::getLicenseFileId)
                            .in(SmartAuthLicensePO::getId, deleteFileIds)
            ).stream().map(SmartAuthLicensePO::getLicenseFileId).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(fileIds)) {
                this.fileService.batchDelete(fileIds);
            }
            this.update(
                    new UpdateWrapper<SmartAuthLicensePO>().lambda()
                            .set(SmartAuthLicensePO::getLicenseFileId, null)
                            .in(SmartAuthLicensePO::getId, deleteFileIds)
            );
        }
        return result;
    }

    /**
     * 批量删除(jdbc批量提交)
     *
     * @param list 主键ID或实体列表(主键ID类型必须与实体类型字段保持一致)
     * @return 删除结果
     * @since 3.5.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        Set<Long> fileIds = this.list(
                new QueryWrapper<SmartAuthLicensePO>().lambda()
                        .select(SmartAuthLicensePO::getLicenseFileId)
                        .in(SmartAuthLicensePO::getId, list)
        ).stream().map(SmartAuthLicensePO::getLicenseFileId).collect(Collectors.toSet());

        boolean result = super.removeBatchByIds(list);
        if (!CollectionUtils.isEmpty(fileIds)) {
            this.fileService.batchDelete(fileIds);
        }
        return result;
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SmartAuthLicensePO entity) {
        Long fileId = null;
        if (LicenseStatusEnum.GENERATOR.equals(entity.getStatus())) {
            fileId = entity.getFileStorageId();
        }
        entity.setFileStorageId(null);
        entity.setStatus(LicenseStatusEnum.UPDATE);
        boolean result = super.save(entity);
        if (fileId != null) {
            this.fileService.delete(entity.getLicenseFileId());
        }
        return result;
    }

    protected SmartAuthSecretKeyPO getSecretKey(Long secretKeyId) {
        SmartAuthSecretKeyPO smartAuthSecretKey = this.smartAuthSecretKeyService.getById(secretKeyId);
        if (smartAuthSecretKey == null) {
            throw new BusinessException("获取秘钥失败，请检查秘钥是否已删除");
        }
        if (!Boolean.TRUE.equals(smartAuthSecretKey.getUseYn())) {
            throw new BusinessException("秘钥已停止使用");
        }
        return smartAuthSecretKey;
    }
}
