package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.constants.UserDeptIdentEnum;
import com.smart.system.mapper.SysDeptMapper;
import com.smart.system.model.SysDeptPO;
import com.smart.system.model.SysUserDeptPO;
import com.smart.system.pojo.vo.SysDeptListVo;
import com.smart.system.service.SysDeptService;
import com.smart.system.service.SysUserDeptService;
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

    private static final Long TOP_PARENT_ID = 0L;

    private final UserSetterService userSetterService;

    private final SysUserDeptService sysUserDeptService;

    public SysDeptServiceImpl(UserSetterService userSetterService, SysUserDeptService sysUserDeptService) {
        this.userSetterService = userSetterService;
        this.sysUserDeptService = sysUserDeptService;
    }

    @Override
    public List<? extends SysDeptPO> list(@NonNull QueryWrapper<SysDeptPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SysDeptPO> userList = super.list(queryWrapper, parameter, paging);
        if (userList.isEmpty()) {
            return userList;
        }
        // 转为volist
        List<SysDeptListVo> voList = userList.stream()
                .map(item -> {
                    SysDeptListVo vo = new SysDeptListVo();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).collect(Collectors.toList());
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
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        if (idList.size() > 1) {
            throw new UnsupportedOperationException("不支持批量删除");
        }
        SysDeptPO dept = this.getById((Serializable) idList.iterator().next());
        if (dept == null) {
            return false;
        }
        Set<Long> parentIds = new HashSet<>((Collection<Long>) idList);
        Set<Long> deleteIds = this.queryAllChildIds(parentIds);
        deleteIds.addAll(parentIds);
        boolean result = super.removeByIds(deleteIds);
        // 更新hasChild
        if (!TOP_PARENT_ID.equals(dept.getParentId())) {
            this.baseMapper.updateHasChild(dept.getParentId());
        }
        // 删除用户部门关联关系
        this.sysUserDeptService.remove(
                new QueryWrapper<SysUserDeptPO>().lambda()
                        .in(SysUserDeptPO::getDeptId, deleteIds)
                        .eq(SysUserDeptPO::getIdent, UserDeptIdentEnum.USER_DEPT.name())
        );
        return result;
    }

    @NonNull
    @Override
    public Set<Long> queryAllChildIds(@NonNull Set<Long> parentIds) {
        Set<Long> childIds = new HashSet<>();
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
        List<SysDeptPO> deptList = this.list(
                new QueryWrapper<SysDeptPO>().lambda()
                        .select(SysDeptPO::getDeptId, SysDeptPO::getHasChild)
                        .in(SysDeptPO::getParentId, idList)
        );
        // 查询下级编码
        Set<Long> ids = deptList.stream().map(SysDeptPO::getDeptId).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(ids)) {
            childIds.addAll(ids);
        }
        // 获取拥有下child
        Set<Long> hasChildIds = deptList.stream()
                .filter(item -> Boolean.TRUE.equals(item.getHasChild()))
                .map(SysDeptPO::getDeptId)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(hasChildIds)) {
            this.queryAllChildId(hasChildIds, childIds);
        }
    }

    /**
     * 重写函数查询上级等信息
     * @param id ID
     * @return 查询结果
     */
    @Override
    public SysDeptPO getById(Serializable id) {
        SysDeptPO dept = super.getById(id);
        if (dept == null) {
            return null;
        }
        SysDeptListVo vo = new SysDeptListVo();
        BeanUtils.copyProperties(dept, vo);
        // 查询创建人更新人信息
//        this.userSetterService.setCreateUpdateUser(ImmutableList.of(vo));
        // 查询上级
        if (vo.getParentId() != 0) {
            vo.setParentDept(super.getById(vo.getParentId()));
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(@NonNull SysDeptPO entity) {
        boolean result = super.save(entity);
        if (entity.getParentId() != null && !TOP_PARENT_ID.equals(entity.getParentId())) {
            this.baseMapper.updateHasChild(entity.getParentId());
        }
        return result;
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<SysDeptPO> entityList) {
        boolean result = super.saveOrUpdateBatch(entityList);
        List<Long> parentIdList = entityList.stream()
                .map(item -> {
                    if (item.getParentId() != null && !TOP_PARENT_ID.equals(item.getParentId())) {
                        return item.getParentId();
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(parentIdList)) {
            parentIdList.forEach(item -> this.baseMapper.updateHasChild(item));
        }
        return result;
    }
}
