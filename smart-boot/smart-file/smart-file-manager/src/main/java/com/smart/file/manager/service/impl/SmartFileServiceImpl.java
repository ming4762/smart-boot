package com.smart.file.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.file.manager.mapper.SmartFileMapper;
import com.smart.file.manager.model.SmartFilePO;
import com.smart.file.manager.model.SmartFileStoragePO;
import com.smart.file.manager.pojo.vo.SmartFileListVO;
import com.smart.file.manager.service.SmartFileService;
import com.smart.file.manager.service.SmartFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文件处理层实现类
 * @author shizhongming
 * 2020/1/27 7:50 下午
 */
@Slf4j
@Service
public class SmartFileServiceImpl extends BaseServiceImpl<SmartFileMapper, SmartFilePO> implements SmartFileService {

    private final SmartFileStorageService smartFileStorageService;

    public SmartFileServiceImpl(SmartFileStorageService smartFileStorageService) {
        this.smartFileStorageService = smartFileStorageService;
    }

    @Override
    public List<? extends SmartFilePO> list(@NonNull QueryWrapper<SmartFilePO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SmartFilePO> dataList = super.list(queryWrapper, parameter, paging);
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.WITH_ALL.name()))) {
            Set<Long> fileStorageIds = dataList.stream().map(SmartFilePO::getFileStorageId).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(fileStorageIds)) {
                Map<Long, SmartFileStoragePO> fileStorageMap = this.smartFileStorageService.listByIds(fileStorageIds).stream()
                        .collect(Collectors.toMap(SmartFileStoragePO::getId, item -> item));
                return dataList.stream()
                        .map(item -> {
                            SmartFileListVO vo = new SmartFileListVO();
                            BeanUtils.copyProperties(item, vo);
                            vo.setFileStorage(fileStorageMap.get(vo.getFileStorageId()));
                            return vo;
                        }).collect(Collectors.toList());
            }
        }
        return dataList;
    }
}
