package com.smart.module.api.file;

import com.smart.module.api.file.dto.SmartFileStorageListDTO;
import org.springframework.util.CollectionUtils;

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

    /**
     * 通过ID查询
     * @param id ID
     * @return 文件存储器
     */
    default SmartFileStorageListDTO getById(Long id) {
        List<SmartFileStorageListDTO> list = listByIds(List.of(id));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.getFirst();
    }


    /**
     * 通过代码查询列表
     * @param codeList 代码列表
     * @return 文件存储器列表
     */
    List<SmartFileStorageListDTO> listByCode(Collection<String> codeList);

    /**
     * 通过代码查询
     * @param code 代码
     * @return 文件存储器
     */
    default SmartFileStorageListDTO getByCode(String code) {
        List<SmartFileStorageListDTO> list = listByCode(List.of(code));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.getFirst();
    }
}
