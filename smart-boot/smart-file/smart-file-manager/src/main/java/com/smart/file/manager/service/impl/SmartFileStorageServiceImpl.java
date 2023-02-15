package com.smart.file.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.ImmutableList;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.file.manager.mapper.SmartFileStorageMapper;
import com.smart.file.manager.model.SmartFileStoragePO;
import com.smart.file.manager.service.SmartFileStorageService;
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
}