package com.smart.system.api.local;

import com.smart.module.api.system.SysDictApi;
import com.smart.module.api.system.dto.SysDictItemDTO;
import com.smart.system.model.SysDictItemPO;
import com.smart.system.service.SysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author zhongming4762
 * 2023/6/1
 */
@Component
@Primary
public class LocalSysDictApi implements SysDictApi {

    private final SysDictService sysDictService;

    public LocalSysDictApi(SysDictService sysDictService) {
        this.sysDictService = sysDictService;
    }

    /**
     * 通过字典编码查询字典项
     *
     * @param dictCode 字典编码
     * @return 字典项
     */
    @Override
    public List<SysDictItemDTO> listByDictCode(String dictCode) {
        return Optional.ofNullable(this.listByDictCode(List.of(dictCode)).get(dictCode))
                .orElse(new ArrayList<>(0));
    }

    /**
     * 通过字典编码批量查询字典项
     *
     * @param dictCode 字典编
     * @return 字典编码为key，字典项为value的list
     */
    @Override
    public Map<String, List<SysDictItemDTO>> listByDictCode(List<String> dictCode) {
        Map<String, List<SysDictItemPO>> listMap = this.sysDictService.listItemByCode(dictCode);
        if (CollectionUtils.isEmpty(listMap)) {
            return new HashMap<>(0);
        }
        Map<String, List<SysDictItemDTO>> result = new HashMap<>(listMap.size());
        listMap.forEach((key, value) -> {
            List<SysDictItemDTO> dtoList = value.stream()
                    .map(item -> {
                        SysDictItemDTO dto = new SysDictItemDTO();
                        BeanUtils.copyProperties(item, dto);
                        dto.setDictCode(key);
                        return dto;
                    }).toList();
            result.put(key, dtoList);
        });
        return result;
    }
}
