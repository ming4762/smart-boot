package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.smart.system.model.SysExceptionPO;
import com.smart.system.service.SysExceptionService;
import com.smart.system.mapper.SysExceptionMapper;

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
}
