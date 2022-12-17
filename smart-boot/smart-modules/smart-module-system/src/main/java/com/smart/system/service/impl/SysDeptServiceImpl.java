package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.mapper.SysDeptMapper;
import com.smart.system.model.SysDeptPO;
import com.smart.system.pojo.vo.SysDeptListVo;
import com.smart.system.service.SysDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
* sys_dept - 部门表 Service实现类
* @author GCCodeGenerator
* 2022年10月13日 上午10:24:21
*/
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDeptPO> implements SysDeptService {

    private final UserSetterService userSetterService;

    public SysDeptServiceImpl(UserSetterService userSetterService) {
        this.userSetterService = userSetterService;
    }

    @Override
    public List<? extends SysDeptPO> list(@NonNull QueryWrapper<SysDeptPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        var userList = super.list(queryWrapper, parameter, paging);
        if (userList.isEmpty()) {
            return userList;
        }
        // 转为volist
        var voList = userList.stream()
                .map(item -> {
                    var vo = new SysDeptListVo();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).toList();
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            // 查询创建 修改人
            this.userSetterService.setCreateUpdateUser(voList);
        }
        return voList;
    }

    /**
     * 重写删除操作 删除下级
     * @param idList ID列表
     * @return 删除是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        Set<Long> parentIds = new HashSet<>((Collection<? extends Long>) idList);
        Set<Long> deleteIds = this.queryAllChildIds(parentIds);
        deleteIds.addAll(parentIds);
        return super.removeByIds(deleteIds);
    }

    @NonNull
    @Override
    public Set<Long> queryAllChildIds(@NonNull Set<Long> parentIds) {
        var childIds = new HashSet<Long>();
        this.queryAllChildId(parentIds, childIds);
        return childIds;
    }

    /**
     * 使用递归查询所有下级ID
     * @param idList 上级ID列表
     * @param childIds 存储下级ID的set
     */
    protected void queryAllChildId(Collection<Long> idList, Set<Long> childIds) {
        if (idList.isEmpty()) {
            return;
        }
        // 查询下级编码
        var ids = this.list(
                new QueryWrapper<SysDeptPO>().lambda()
                        .select(SysDeptPO::getDeptId)
                        .in(SysDeptPO::getParentId, idList)
        ).stream().map(SysDeptPO::getDeptId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(ids)) {
            childIds.addAll(ids);
            this.queryAllChildId(ids, childIds);
        }
    }

    /**
     * 重写函数查询上级等信息
     * @param id ID
     * @return 查询结果
     */
    @Override
    public SysDeptPO getById(Serializable id) {
        var dept = super.getById(id);
        if (dept == null) {
            return null;
        }
        var vo = new SysDeptListVo();
        BeanUtils.copyProperties(dept, vo);
        // 查询创建人更新人信息
        this.userSetterService.setCreateUpdateUser(List.of(vo));
        // 查询上级
        if (vo.getParentId() != 0) {
            vo.setParentDept(super.getById(vo.getParentId()));
        }
        return vo;
    }
}
