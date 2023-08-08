package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.utils.CrudUtils;
import com.smart.system.mapper.CommonMapper;
import com.smart.system.mapper.SysCategoryMapper;
import com.smart.system.model.SysCategoryPO;
import com.smart.system.service.SysCategoryService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* sys_category - 分类字段 Service实现类
* @author SmartCodeGenerator
* 2023-1-21 21:32:15
*/
@Service
public class SysCategoryServiceImpl extends BaseServiceImpl<SysCategoryMapper, SysCategoryPO> implements SysCategoryService {

    private final CommonMapper commonMapper;

    public SysCategoryServiceImpl(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    /**
     * 重写save方法，修改ID的生成策略
     *
     * @param entity 实体类
     * @return 是否保存成功
     * @author jackson
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(@NonNull SysCategoryPO entity) {
        boolean result = super.save(entity);
        if (result) {
            // 设置上级hasChild
            this.update(
                    new UpdateWrapper<SysCategoryPO>().lambda()
                            .set(SysCategoryPO::getHasChild, true)
                            .eq(SysCategoryPO::getId, entity.getParentId())
            );
        }
        return result;
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
        this.commonMapper.updateHasChild(
                this.getTableName(),
                CrudUtils.getDbField(SysCategoryPO::getParentId),
                CrudUtils.getDbField(SysCategoryPO::getId),
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
            ).stream().map(SysCategoryPO::getId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(children)) {
                this.deleteTree(children, deleteIds);
            }
        }
    }
}