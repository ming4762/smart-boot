package com.smart.file.manager.service;

import com.smart.crud.service.BaseService;
import com.smart.file.manager.model.SmartFileStoragePO;

import java.io.Serializable;

/**
* smart_file_storage - 文件存储器配置 Service
* @author SmartCodeGenerator
* 2023-2-14
*/
public interface SmartFileStorageService extends BaseService<SmartFileStoragePO> {

    /**
     * 设置默认
     * @param id ID
     * @return 修改结果
     */
    boolean setDefault(Serializable id);

    /**
     * 通过code查询
     * @param code code
     * @return 文件存储器信息
     */
    SmartFileStoragePO getByCode(String code);

    /**
     * 获取默认的存储器
     * @return 默认存储器
     */
    SmartFileStoragePO getDefault();
}