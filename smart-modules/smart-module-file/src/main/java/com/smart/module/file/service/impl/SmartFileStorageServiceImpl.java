package com.smart.module.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.file.mapper.SmartFileStorageMapper;
import com.smart.module.file.model.SmartFileStoragePO;
import com.smart.module.file.service.SmartFileStorageService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
* smart_file_storage - 文件存储器配置 Service实现类
* @author SmartCodeGenerator
* 2023-2-14
*/
@Service
public class SmartFileStorageServiceImpl extends BaseServiceImpl<SmartFileStorageMapper, SmartFileStoragePO> implements SmartFileStorageService {

    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<? extends SmartFileStoragePO> list(@NonNull QueryWrapper<SmartFileStoragePO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.lambda()
                .select(SmartFileStoragePO.class, tableInfo ->
                        !tableInfo.getProperty().equals(CrudUtils.getJavaProperty(SmartFileStoragePO::getPrivateKey))
                       && !tableInfo.getProperty().equals(CrudUtils.getJavaProperty(SmartFileStoragePO::getPublicKey))
                );
        return super.list(queryWrapper, parameter, paging);
    }

    /**
     * 设置默认
     *
     * @param id ID
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(Serializable id) {
        this.update(
                new UpdateWrapper<SmartFileStoragePO>().lambda()
                        .set(SmartFileStoragePO::getDefaultStorage, false)
                        .notIn(SmartFileStoragePO::getId, List.of(id))
        );
        this.update(
                new UpdateWrapper<SmartFileStoragePO>().lambda()
                        .set(SmartFileStoragePO::getDefaultStorage, true)
                        .eq(SmartFileStoragePO::getId, id)
        );
        return true;
    }

    /**
     * 通过code查询
     *
     * @param code code
     * @return 文件存储器信息
     */
    @Override
    public SmartFileStoragePO getByCode(String code) {
        return this.getOne(
                new QueryWrapper<SmartFileStoragePO>().lambda()
                        .eq(SmartFileStoragePO::getStorageCode, code)
        );
    }

    /**
     * 获取默认的存储器
     *
     * @return 默认存储器
     */
    @Override
    public SmartFileStoragePO getDefault() {
        return this.getOne(
                new QueryWrapper<SmartFileStoragePO>().lambda()
                        .eq(SmartFileStoragePO::getDefaultStorage, true)
        );
    }

    /**
     * 设置为加密存储器
     *
     * @param fileStorageIdList 文件存储ID列表
     * @return 是否设置成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setEncrypt(List<Long> fileStorageIdList) {
        if (CollectionUtils.isEmpty(fileStorageIdList)) {
            return false;
        }
        return this.update(
                Wrappers.lambdaUpdate(this.getEntityClass())
                        .set(SmartFileStoragePO::getEncryptedYn, Boolean.TRUE)
                        .in(SmartFileStoragePO::getId, fileStorageIdList)
        );
    }
}