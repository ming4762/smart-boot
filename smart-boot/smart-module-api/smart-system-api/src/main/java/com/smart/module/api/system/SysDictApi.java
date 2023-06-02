package com.smart.module.api.system;

import com.smart.module.api.system.dto.SysDictItemDTO;

import java.util.List;
import java.util.Map;

/**
 * 系统字典API
 * @author zhongming4762
 * 2023/6/1
 */
public interface SysDictApi {

    /**
     * 通过字典编码查询字典项
     * @param dictCode 字典编码
     * @return 字典项
     */
    List<SysDictItemDTO> listByDictCode(String dictCode);

    /**
     * 通过字典编码批量查询字典项
     * @param dictCode 字典编
     * @return 字典编码为key，字典项为value的list
     */
    Map<String, List<SysDictItemDTO>> listByDictCode(List<String> dictCode);

}
