package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.SysDictMapper;
import com.smart.system.model.SysDictItemPO;
import com.smart.system.model.SysDictPO;
import com.smart.system.service.SysDictItemService;
import com.smart.system.service.SysDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* sys_dict - 系统字典表 Service实现类
* @author GCCodeGenerator
* 2022-1-29 10:34:36
*/
@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDictPO> implements SysDictService {

    private final SysDictItemService sysDictItemService;

    public SysDictServiceImpl(SysDictItemService sysDictItemService) {
        this.sysDictItemService = sysDictItemService;
    }

    /**
     * 重写删除函数
     * 删除字典的同时也删除字典项
     * @param idList ID列表
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 删除字典项
        Lists.partition(new ArrayList<>(idList), 200).forEach(list -> this.sysDictItemService.remove(
                new QueryWrapper<SysDictItemPO>().lambda()
                        .in(SysDictItemPO::getDictId, list)
        ));
        return super.removeByIds(idList);
    }

    /**
     * 通过code查询item
     *
     * @param dictCode dict code
     * @return dict item list
     */
    @Override
    public List<SysDictItemPO> listItemByCode(String dictCode) {
        return Optional.ofNullable(this.listItemByCode(List.of(dictCode)).get(dictCode))
                .orElse(new ArrayList<>(0));
    }

    /**
     * 通过code批量查询item
     *
     * @param dictCodeList 字典编码列表
     * @return 字典编码为key，字典项为value的list
     */
    @Override
    public Map<String, List<SysDictItemPO>> listItemByCode(List<String> dictCodeList) {
        if (CollectionUtils.isEmpty(dictCodeList)) {
            return Collections.emptyMap();
        }
        List<SysDictPO> dictList = this.list(
                new QueryWrapper<SysDictPO>().lambda()
                        .select(SysDictPO::getId, SysDictPO::getDictCode)
                        .in(SysDictPO::getDictCode, dictCodeList)
                        .eq(SysDictPO::getUseYn, Boolean.TRUE)
        );
        if (CollectionUtils.isEmpty(dictList)) {
            return Collections.emptyMap();
        }
        Map<Long, String> dictIdCodeMap = dictList.stream()
                .collect(Collectors.toMap(SysDictPO::getId, SysDictPO::getDictCode));
        return this.sysDictItemService.list(
                new QueryWrapper<SysDictItemPO>().lambda()
                        .in(SysDictItemPO::getDictId, dictIdCodeMap.keySet())
                        .eq(SysDictItemPO::getUseYn, Boolean.TRUE)
                        .orderByAsc(SysDictItemPO::getSeq)
        ).stream()
                .collect(Collectors.groupingBy(item -> dictIdCodeMap.get(item.getDictId())));
    }
}
