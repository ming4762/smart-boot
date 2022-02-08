package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.pojo.dto.dict.SysDictItemIdDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.smart.system.model.SysDictItemPO;
import com.smart.system.service.SysDictItemService;
import com.smart.system.mapper.SysDictItemMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* sys_dict_item - 字典序表 Service实现类
* @author GCCodeGenerator
* 2022-2-7 10:48:32
*/
@Service
public class SysDictItemServiceImpl extends BaseServiceImpl<SysDictItemMapper, SysDictItemPO> implements SysDictItemService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchDelete(@NonNull List<SysDictItemIdDTO> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        Lists.partition(idList, 200).forEach(list -> {
            LambdaQueryWrapper<SysDictItemPO> queryWrapper = new QueryWrapper<SysDictItemPO>().lambda();
            list.forEach(item -> queryWrapper.or(wrapper -> wrapper.eq(SysDictItemPO :: getDictCode, item.getDictCode()).eq(SysDictItemPO :: getDictItemCode, item.getDictItemCode())));
            this.remove(queryWrapper);
        });
        return true;
    }
}
