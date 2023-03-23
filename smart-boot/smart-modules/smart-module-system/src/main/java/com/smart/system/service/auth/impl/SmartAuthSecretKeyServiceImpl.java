package com.smart.system.service.auth.impl;

import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.exception.BaseException;
import com.smart.commons.core.exception.BusinessException;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.module.api.file.SmartFileApi;
import com.smart.module.api.file.SmartFileStorageApi;
import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import com.smart.module.api.file.dto.SmartFileStorageListDTO;
import com.smart.module.api.system.constants.SysParameterCodeEnum;
import com.smart.system.mapper.auth.SmartAuthSecretKeyMapper;
import com.smart.system.model.auth.SmartAuthSecretKeyPO;
import com.smart.system.pojo.dto.auth.SmartAuthSecretKeyUploadUpdateDTO;
import com.smart.system.pojo.vo.auth.SmartAuthSecretKeyListVO;
import com.smart.system.service.SysParameterService;
import com.smart.system.service.auth.SmartAuthSecretKeyService;
import jakarta.servlet.ServletOutputStream;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
* smart_auth_secret_key - 秘钥管理 Service实现类
* @author SmartCodeGenerator
* 2023-2-19 10:57:38
*/
@Service
public class SmartAuthSecretKeyServiceImpl extends BaseServiceImpl<SmartAuthSecretKeyMapper, SmartAuthSecretKeyPO> implements SmartAuthSecretKeyService {

    private final SmartFileApi smartFileApi;

    private final SmartFileStorageApi smartFileStorageApi;

    private final SysParameterService sysParameterService;

    public SmartAuthSecretKeyServiceImpl(SmartFileApi smartFileApi, SysParameterService sysParameterService, SmartFileStorageApi smartFileStorageApi) {
        this.smartFileApi = smartFileApi;
        this.sysParameterService = sysParameterService;
        this.smartFileStorageApi = smartFileStorageApi;
    }

    @Override
    public List<? extends SmartAuthSecretKeyPO> list(@NonNull QueryWrapper<SmartAuthSecretKeyPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SmartAuthSecretKeyPO> list = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.WITH_ALL.name()))) {
            Set<Long> fileStorageIds = list.stream().map(SmartAuthSecretKeyPO::getFileStorageId).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(fileStorageIds)) {
                Map<Long, SmartFileStorageListDTO> fileStorageMap = this.smartFileStorageApi.listByIds(fileStorageIds).stream()
                        .collect(Collectors.toMap(SmartFileStorageListDTO::getId, item -> item));
                return list.stream()
                        .map(item -> {
                            SmartAuthSecretKeyListVO vo = new SmartAuthSecretKeyListVO();
                            BeanUtils.copyProperties(item, vo);
                            vo.setFileStorage(fileStorageMap.get(item.getFileStorageId()));
                            return vo;
                        }).toList();
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        Set<Long> fileIdList = this.list(
                new QueryWrapper<SmartAuthSecretKeyPO>().lambda()
                        .select(SmartAuthSecretKeyPO::getPublicKeyFileId, SmartAuthSecretKeyPO::getPrivateKeyFileId)
                        .in(SmartAuthSecretKeyPO::getId, list)
        ).stream().flatMap(item -> Stream.of(item.getPublicKeyFileId(), item.getPrivateKeyFileId())).collect(Collectors.toSet());
        boolean result = super.removeBatchByIds(list);
        // 删除对应的文件信息
        if (!CollectionUtils.isEmpty(fileIdList)) {
            this.smartFileApi.batchDelete(fileIdList);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUpdate(SmartAuthSecretKeyUploadUpdateDTO parameter) {
        SmartAuthSecretKeyPO secretKey = null;
        if (parameter.getId() != null) {
            secretKey = this.getById(parameter.getId());
        }
        String secretFileType = this.sysParameterService.getParameter(SysParameterCodeEnum.SECRECY_FILE_TYPE.getCode());
        // 第一步保存文件
        FileHandlerResult publicKeySaveResult = null;
        FileHandlerResult privateKeySaveResult = null;
        try {
            publicKeySaveResult = this.smartFileApi.save(
                        RemoteFileSaveParameter.builder()
                            .multipartFile(parameter.getPublicKeyFile())
                            .fileStorageId(parameter.getFileStorageId())
                            .type(secretFileType)
                            .build()
            );
            privateKeySaveResult = this.smartFileApi.save(
                    RemoteFileSaveParameter.builder()
                            .multipartFile(parameter.getPrivateKeyFile())
                            .fileStorageId(parameter.getFileStorageId())
                            .type(secretFileType)
                            .build()
            );
            SmartAuthSecretKeyPO model = new SmartAuthSecretKeyPO();
            BeanUtils.copyProperties(parameter, model);
            model.setPrivateKeyFileId(privateKeySaveResult.getFileId());
            model.setPublicKeyFileId(publicKeySaveResult.getFileId());
            boolean saveResult = this.save(model);
            // 删除之前的文件，放到最后是因为防止保存失败，数据回蓝，而文件已经删除
            if (secretKey != null) {
                this.smartFileApi.batchDelete(List.of(secretKey.getPrivateKeyFileId(), secretKey.getPublicKeyFileId()));
            }
            return saveResult;
        } catch (Exception e) {
            if (publicKeySaveResult != null) {
                this.smartFileApi.delete(publicKeySaveResult.getFileId());
            }
            if (privateKeySaveResult != null) {
                this.smartFileApi.delete(privateKeySaveResult.getFileId());
            }
            throw new BaseException(e);
        }
    }

    /**
     * 下载秘钥
     *
     * @param id           ID
     * @param outputStream 输出流
     */
    @Override
    public void download(Serializable id, ServletOutputStream outputStream) {
        SmartAuthSecretKeyPO secretKey = this.getById(id);
        if (secretKey == null) {
            throw new BusinessException("获取秘钥信息失败，请检查ID是否正确");
        }
        // 获取文件
        FileDownloadResult publicKey = this.smartFileApi.download(secretKey.getPublicKeyFileId());
        FileDownloadResult privateKey = this.smartFileApi.download(secretKey.getPrivateKeyFileId());

        ZipUtil.zip(
                outputStream,
                new String[]{publicKey.getFilename(), privateKey.getFilename()},
                new InputStream[]{publicKey.getInputStream(), privateKey.getInputStream()}
        );
    }
}