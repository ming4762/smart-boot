package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.SysExceptionMapper;
import com.smart.system.model.SysExceptionPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.dto.exception.ExceptionFeedbackDTO;
import com.smart.system.pojo.dto.exception.SysExceptionMarkResolvedParameter;
import com.smart.system.pojo.vo.SysExceptionListVO;
import com.smart.system.service.SysExceptionService;
import com.smart.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
* sys_exception - 系统异常信息 Service实现类
* @author GCCodeGenerator
* 2022年6月10日
*/
@Service
public class SysExceptionServiceImpl extends BaseServiceImpl<SysExceptionMapper, SysExceptionPO> implements SysExceptionService {

    private final SysUserService sysUserService;

    public SysExceptionServiceImpl(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public List<? extends SysExceptionPO> list(@NonNull QueryWrapper<SysExceptionPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.select(SysExceptionPO.class, field -> !"stackTrace".equals(field.getProperty()));
        List<? extends SysExceptionPO> list = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<SysExceptionListVO> voList = list.stream()
                .map(item -> {
                    SysExceptionListVO vo = new SysExceptionListVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).toList();
        this.queryResolvedUser(voList);
        return voList;
    }

    private void queryResolvedUser(List<SysExceptionListVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        Set<Long> userIds = voList.stream()
                .map(SysExceptionPO::getResolvedUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        Map<Long, SysUserPO> sysUserMap = this.sysUserService.listByIds(userIds)
                .stream()
                .collect(Collectors.toMap(SysUserPO::getUserId, item -> item));
        if (CollectionUtils.isEmpty(sysUserMap)) {
            return;
        }
        voList.forEach(item -> item.setResolvedUser(sysUserMap.get(item.getResolvedUserId())));
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

    /**
     * 标记异常已解决
     *
     * @param parameter 参数
     * @return 是否操作成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markResolved(SysExceptionMarkResolvedParameter parameter) {
        if (CollectionUtils.isEmpty(parameter.getExceptionIdList())) {
            return false;
        }
        return this.update(
                new UpdateWrapper<SysExceptionPO>().lambda()
                        .set(SysExceptionPO::getResolved, Boolean.TRUE)
                        .set(SysExceptionPO::getResolvedTime, LocalDateTime.now())
                        .set(SysExceptionPO::getResolvedMessage, parameter.getResolvedMessage())
                        .set(SysExceptionPO::getResolvedUserId, AuthUtils.getNonNullCurrentUserId())
                        .in(SysExceptionPO::getId, parameter.getExceptionIdList())
        );
    }
}
