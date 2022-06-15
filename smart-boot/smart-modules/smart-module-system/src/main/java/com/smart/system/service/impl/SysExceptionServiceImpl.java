package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.pojo.dto.exception.ExceptionFeedbackDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.smart.system.model.SysExceptionPO;
import com.smart.system.service.SysExceptionService;
import com.smart.system.mapper.SysExceptionMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
* sys_exception - 系统异常信息 Service实现类
* @author GCCodeGenerator
* 2022年6月10日
*/
@Service
public class SysExceptionServiceImpl extends BaseServiceImpl<SysExceptionMapper, SysExceptionPO> implements SysExceptionService {

    @Override
    public List<? extends SysExceptionPO> list(@NonNull QueryWrapper<SysExceptionPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.select(SysExceptionPO.class, field -> !"stackTrace".equals(field.getProperty()));
        return super.list(queryWrapper, parameter, paging);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean feedback(ExceptionFeedbackDTO parameter) {
        if (CollectionUtils.isEmpty(parameter.getIdList())) {
            return false;
        }
        return this.update(
                new UpdateWrapper<SysExceptionPO>().lambda()
                        .set(SysExceptionPO::getFeedbackMessage, parameter.getFeedbackMessage())
                        .set(SysExceptionPO::getUserFeedback, true)
                        .set(SysExceptionPO::getFeedbackTime, LocalDateTime.now())
                        .in(SysExceptionPO::getId, parameter.getIdList())
        );
    }
}
