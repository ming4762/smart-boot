package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysDictItemPO;
import com.smart.system.model.SysDictPO;

import java.util.List;

/**
* sys_dict - 系统字典表 Service
* @author GCCodeGenerator
* 2022-1-29 10:34:36
*/
public interface SysDictService extends BaseService<SysDictPO> {

    /**
     * 通过code查询item
     * @param dictCode dict code
     * @return dict item list
     */
    List<SysDictItemPO> listItemByCode(String dictCode);
}
