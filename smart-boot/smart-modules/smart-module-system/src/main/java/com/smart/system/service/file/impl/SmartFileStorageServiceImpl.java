package com.smart.system.service.file.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.ImmutableList;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.file.SmartFileStorageMapper;
import com.smart.system.model.file.SmartFileStoragePO;
import com.smart.system.service.file.SmartFileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
* smart_file_storage - 文件存储器配置 Service实现类
* @author SmartCodeGenerator
* 2023-2-14
*/
@Service
public class SmartFileStorageServiceImpl extends BaseServiceImpl<SmartFileStorageMapper, SmartFileStoragePO> implements SmartFileStorageService {

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
                        .notIn(SmartFileStoragePO::getId, ImmutableList.of(id))
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
}