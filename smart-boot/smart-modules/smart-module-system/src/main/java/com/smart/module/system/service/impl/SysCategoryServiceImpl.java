package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.mapper.CommonMapper;
import com.smart.module.system.mapper.SysCategoryMapper;
import com.smart.module.system.model.SysCategoryPO;
import com.smart.module.system.service.SysCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* sys_category - 分类字段 Service实现类
* @author SmartCodeGenerator
* 2023-1-21 21:32:15
*/
@Service
@RequiredArgsConstructor
public class SysCategoryServiceImpl extends BaseServiceImpl<SysCategoryMapper, SysCategoryPO> implements SysCategoryService {

    private final CommonMapper commonMapper;

    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<? extends SysCategoryPO> list(@NonNull QueryWrapper<SysCategoryPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (Boolean.TRUE.equals(parameter.getParameter().get(SystemConstantEnum.LIST_FILTER_TENANT))) {
            queryWrapper.lambda()
                    .and(query -> {
                        query.eq(SysCategoryPO::getTenantId, AuthUtils.getNonNullCurrentTenantId());
                        if (AuthUtils.isPlatformTenant()) {
                            query.or(wrapper -> wrapper.eq(SysCategoryPO::getTenantCommonYn, Boolean.TRUE));
                        }
                    });
        }

        return super.list(queryWrapper, parameter, paging);
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(SysCategoryPO entity) {
        boolean isAdd = this.isAdd(entity);
        if (!isAdd) {
            return this.updateById(entity);
        }
        this.save(entity);
        // 更新上级hasChild
        SmartTableInfo tableInfo = this.getTableInfo();
        this.commonMapper.updateHasChild(
                this.getTableName(),
                tableInfo.getTableFiled(SysCategoryPO::getParentId).getColumn(),
                tableInfo.getTableFiled(SysCategoryPO::getId).getColumn(),
                entity.getParentId()
        );
        return true;
    }

    /**
     * 批量删除
     *
     * @param list    主键ID或实体列表
     * @return 删除结果
     * @since 3.5.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        if (list.size() > 1) {
            throw new UnsupportedOperationException("不支持批量删除");
        }
        // 查询上级ID
        SysCategoryPO deleteData = this.getById((Serializable) list.iterator().next());
        HashSet<Serializable> deleteIds = new HashSet<>();
        this.deleteTree((Collection<? extends Serializable>) list, deleteIds);
        boolean result = super.removeByIds(deleteIds);
        // 更新上级hasChild
        SmartTableInfo tableInfo = this.getTableInfo();
        this.commonMapper.updateHasChild(
                this.getTableName(),
                tableInfo.getTableFiled(SysCategoryPO::getParentId).getColumn(),
                tableInfo.getTableFiled(SysCategoryPO::getId).getColumn(),
                deleteData.getParentId()
        );
        return result;
    }

    private void deleteTree(Collection<? extends Serializable> list, Set<Serializable> deleteIds) {
        if (!CollectionUtils.isEmpty(list)) {
            // 查询是否有下级
            deleteIds.addAll(list);
            // 查询是否有下级
            List<Long> children = this.list(
                    new QueryWrapper<SysCategoryPO>().lambda()
                            .select(SysCategoryPO::getId)
                            .in(SysCategoryPO::getParentId, list)
            ).stream().map(SysCategoryPO::getId).toList();
            if (!CollectionUtils.isEmpty(children)) {
                this.deleteTree(children, deleteIds);
            }
        }
    }
}