package com.smart.module.api.file;

import com.smart.module.api.file.dto.SmartFileStorageListDTO;

import java.util.Collection;
import java.util.List;

/**
 * 文件存储器API
 * @author zhongming4762
 * 2023/3/21
 */
public interface SmartFileStorageApi {

    /**
     * 通过ID查询列表
     * @param idList ID列表
     * @return 文件存储器列表
     */
    List<SmartFileStorageListDTO> listByIds(Collection<Long> idList);

}
