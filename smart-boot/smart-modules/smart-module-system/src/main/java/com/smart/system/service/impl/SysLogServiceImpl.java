package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.mapper.SysLogMapper;
import com.smart.system.model.SysLogPO;
import com.smart.system.pojo.vo.SysLogListVO;
import com.smart.system.service.SysLogService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLogPO> implements SysLogService {

    private static final List<String> LIST_NO_SELECT_FIELDS = Lists.newArrayList("params", "errorMessage", "result");

    private final UserSetterService userSetterService;

    public SysLogServiceImpl(UserSetterService userSetterService) {
        this.userSetterService = userSetterService;
    }

    @Override
    public List<? extends SysLogPO> list(@NonNull QueryWrapper<SysLogPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.select(SysLogPO.class, field -> !LIST_NO_SELECT_FIELDS.contains(field.getProperty()));
        List<? extends SysLogPO> sysLogList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(sysLogList)) {
            return Lists.newArrayList();
        }
        List<SysLogListVO> logVoList = sysLogList.stream().map(item -> {
            SysLogListVO vo = new SysLogListVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUser(logVoList);
        }
        return logVoList;
    }
}
