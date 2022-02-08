package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysDictItemPO;
import com.smart.system.pojo.dto.dict.SysDictItemIdDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
* sys_dict_item - 字典序表 Service
* @author GCCodeGenerator
* 2022-2-7 10:48:32
*/
public interface SysDictItemService extends BaseService<SysDictItemPO> {

    /**
     * 批量删除
     * @param idList ID列表
     * @return 是否删除成功
     */
    Boolean batchDelete(@NonNull List<SysDictItemIdDTO> idList);
}
