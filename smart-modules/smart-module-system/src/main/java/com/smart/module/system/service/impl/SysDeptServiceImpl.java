package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.crud.constants.CrudCommonEnum;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.service.UserSetterService;
import com.smart.module.system.constants.UserDeptIdentEnum;
import com.smart.module.system.mapper.CommonMapper;
import com.smart.module.system.mapper.SysDeptMapper;
import com.smart.module.system.model.SysDeptPO;
import com.smart.module.system.model.SysUserDeptPO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.pojo.vo.SysDeptListVO;
import com.smart.module.system.service.SysDeptService;
import com.smart.module.system.service.SysUserDeptService;
import com.smart.module.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDeptPO> implements SysDeptService {

    private static final Long TOP_PARENT_ID = 0L;

    private final UserSetterService userSetterService;
    private final SysUserDeptService sysUserDeptService;
    private final CommonMapper commonMapper;
    private final SysUserService sysUserService;

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
            this.updateHasChild(dept.getParentId());
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
        var dept = super.getById(id);
        if (dept == null) {
            return null;
        }
        var vo = new SysDeptListVO();
        BeanUtils.copyProperties(dept, vo);
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
            this.updateHasChild(entity.getParentId());
        }
        return result;
    }

    private void updateHasChild(Long id) {
        SmartTableInfo tableInfo = this.getTableInfo();
        this.commonMapper.updateHasChild(
                tableInfo.getTableName(),
                tableInfo.getTableFiled(SysDeptPO::getParentId).getColumn(),
                tableInfo.getKeyColumn(),
                id
        );
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
                .toList();
        if (!CollectionUtils.isEmpty(parentIdList)) {
            parentIdList.forEach(this::updateHasChild);
        }
        return result;
    }

    /**
     * 根据部门ID查询用户列表
     *
     * @param deptId 部门ID
     * @return 用户列表
     */
    @Override
    public List<SysUserPO> listUserByDeptId(Long deptId) {
        if (deptId == null) {
            return List.of();
        }
        Set<Long> userIds = this.sysUserDeptService.lambdaQuery()
                .select(SysUserDeptPO::getUserId)
                .eq(SysUserDeptPO::getDeptId, deptId)
                .list().stream()
                .map(SysUserDeptPO::getUserId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(userIds)) {
            return List.of();
        }
        return this.sysUserService.lambdaQuery()
                .in(SysUserPO::getUserId, userIds)
                .eq(SysUserPO::getUseYn, Boolean.TRUE)
                .list();
    }

    /**
     * 获取用户部门
     *
     * @param userId 用户ID
     * @return 部门列表
     */
    @Override
    public List<SysDeptPO> listUserDept(Long userId) {
        if (userId == null) {
            return List.of();
        }
        Set<Long> deptIds = this.sysUserDeptService.lambdaQuery()
                .select(SysUserDeptPO::getDeptId)
                .eq(SysUserDeptPO::getUserId, userId)
                .eq(SysUserDeptPO::getIdent, UserDeptIdentEnum.USER_DEPT)
                .list().stream()
                .map(SysUserDeptPO::getDeptId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(deptIds)) {
            return List.of();
        }
        return this.lambdaQuery()
                .eq(SysDeptPO::getUseYn, Boolean.TRUE)
                .in(SysDeptPO::getDeptId, deptIds)
                .list();
    }
}
