package com.smart.file.manager.api.local;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.utils.BeanUtils;
import com.smart.file.manager.model.SmartFileStoragePO;
import com.smart.file.manager.service.SmartFileStorageService;
import com.smart.module.api.file.SmartFileStorageApi;
import com.smart.module.api.file.dto.SmartFileStorageListDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/21
 */
@Component
@Primary
public class LocalSmartFileStorageApi implements SmartFileStorageApi {

    private final SmartFileStorageService smartFileStorageService;

    public LocalSmartFileStorageApi(SmartFileStorageService smartFileStorageService) {
        this.smartFileStorageService = smartFileStorageService;
    }

    /**
     * 通过ID查询列表
     *
     * @param idList ID列表
     * @return 文件存储器列表
     */
    @Override
    public List<SmartFileStorageListDTO> listByIds(Collection<Long> idList) {
        LambdaQueryWrapper<SmartFileStoragePO> queryWrapper = new QueryWrapper<SmartFileStoragePO>().lambda()
                .eq(SmartFileStoragePO::getUseYn, Boolean.TRUE)
                .orderByAsc(SmartFileStoragePO::getSeq);
        if (idList != null) {
            if (idList.isEmpty()) {
                return Collections.emptyList();
            }
            queryWrapper.in(SmartFileStoragePO::getId, idList);
        }
        List<SmartFileStoragePO> list = this.smartFileStorageService.list(queryWrapper);
        return BeanUtils.copyProperties(list, SmartFileStorageListDTO.class);
    }
}
