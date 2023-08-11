package com.smart.system.service.change.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.utils.IdGenerator;
import com.smart.commons.validate.utils.ValidatorUtils;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.module.api.system.dto.SmartChangeLogListDTO;
import com.smart.module.api.system.parameter.RemoteChangeLogListParameter;
import com.smart.module.api.system.parameter.RemoteChangeLogSaveParameter;
import com.smart.system.mapper.change.SmartChangeLogMapper;
import com.smart.system.model.SmartChangeLogDetailPO;
import com.smart.system.model.SmartChangeLogPO;
import com.smart.system.service.change.SmartChangeLogDetailService;
import com.smart.system.service.change.SmartChangeLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhongming4762
 * 2023/7/31 18:01
 */
@Service
public class SmartChangeLogServiceImpl extends BaseServiceImpl<SmartChangeLogMapper, SmartChangeLogPO> implements SmartChangeLogService {

    private final SmartChangeLogDetailService smartChangeLogDetailService;

    public SmartChangeLogServiceImpl(SmartChangeLogDetailService smartChangeLogDetailService) {
        this.smartChangeLogDetailService = smartChangeLogDetailService;
    }

    /**
     * 保存修改记录
     *
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveChangeLog(RemoteChangeLogSaveParameter parameter) {
        // 校验参数
        ValidatorUtils.validate(parameter);

        long logId = IdGenerator.nextId();
        SmartChangeLogPO changeLog = new SmartChangeLogPO();
        changeLog.setId(logId);
        changeLog.setIdent(parameter.getIdent());
        changeLog.setBusinessId(parameter.getBusinessId());
        changeLog.setBusinessData(parameter.getBusinessData());
        changeLog.setOperateType(parameter.getOperateType());
        changeLog.setParams1(parameter.getParams1());
        changeLog.setParams2(parameter.getParams2());
        changeLog.setParams3(parameter.getParams3());
        this.save(changeLog);
        if (!CollectionUtils.isEmpty(parameter.getDetailList())) {
            this.smartChangeLogDetailService.saveBatch(
                    parameter.getDetailList().stream()
                            .map(item -> {
                                SmartChangeLogDetailPO changeLogDetail = new SmartChangeLogDetailPO();
                                changeLogDetail.setLogId(logId);
                                changeLogDetail.setChangeField(item.getChangeField());
                                changeLogDetail.setBeforeValue(item.getBeforeValue());
                                changeLogDetail.setAfterValue(item.getAfterValue());
                                return changeLogDetail;
                            }).toList()
            );
        }
        return true;
    }

    /**
     * 查询修改记录
     *
     * @param parameter 参数
     * @return 修改记录列表
     */
    @Override
    public List<SmartChangeLogListDTO> listChangeLog(RemoteChangeLogListParameter parameter) {
        ValidatorUtils.validate(parameter);

        LambdaQueryWrapper<SmartChangeLogPO> queryWrapper = new QueryWrapper<SmartChangeLogPO>().lambda()
                .eq(SmartChangeLogPO::getIdent, parameter.getIdent())
                .orderByAsc(SmartChangeLogPO::getCreateTime);
        if (parameter.getBusinessId() != null) {
            queryWrapper.eq(SmartChangeLogPO::getBusinessId, parameter.getBusinessId());
        }
        List<SmartChangeLogPO> logList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(logList)) {
            return Collections.emptyList();
        }
        // 查询详情
        Map<Long, List<SmartChangeLogDetailPO>> detailMap = this.smartChangeLogDetailService.list(
                new QueryWrapper<SmartChangeLogDetailPO>().lambda()
                        .in(
                                SmartChangeLogDetailPO::getLogId,
                                logList.stream().map(SmartChangeLogPO::getId).toList()
                        )
        ).stream().collect(Collectors.groupingBy(SmartChangeLogDetailPO::getLogId));

        return logList.stream()
                .map(item -> {
                    SmartChangeLogListDTO dto = new SmartChangeLogListDTO();
                    BeanUtils.copyProperties(item, dto);
                    List<SmartChangeLogDetailPO> detailPoList = detailMap.get(item.getId());
                    if (!CollectionUtils.isEmpty(detailPoList)) {
                        List<SmartChangeLogListDTO.Detail> detailList = detailMap.get(item.getId()).stream()
                                .map(detail -> {
                                    SmartChangeLogListDTO.Detail detailDto = new SmartChangeLogListDTO.Detail();
                                    BeanUtils.copyProperties(detail, detailDto);
                                    return detailDto;
                                }).toList();
                        dto.setDetailList(detailList);
                    }
                    return dto;
                }).toList();
    }
}
