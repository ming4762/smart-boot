package com.smart.crud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.smart.crud.mapper.CrudBaseMapper;
import com.smart.crud.model.BaseModel;
import com.smart.crud.model.Sort;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.utils.CrudPageHelper;
import com.smart.crud.utils.CrudUtils;
import com.smart.crud.utils.PageCache;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author shizhongming
 * 2020/1/10 9:51 下午
 */
public abstract class BaseServiceImpl<K extends CrudBaseMapper<T>, T extends BaseModel> extends ServiceImpl<K, T> implements BaseService<T> {

    private static final String SORT_ASC = "ASC";

    /**
     * 重写批量删除方法，如果ID只有一个调用removeById方法
     * @param idList ID列表
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        if (idList.size() == 1) {
            return this.removeById((Serializable) idList.iterator().next());
        }
        return super.removeByIds(idList);
    }

    /**
     * 重写方法
     * 防止idList为空时发生错误
     * @param idList 实体类ID列表
     * @return 实体类集合
     */
    @Override
    public List<T> listByIds(Collection<? extends Serializable> idList) {
        if (idList.isEmpty()) {
            return Lists.newArrayList();
        }
        final Set<? extends Serializable> idSet = new HashSet<>(idList);
        if (idSet.size() == 1) {
            final T result = this.getById(idSet.iterator().next());
            return result == null ? Lists.newArrayList() : Lists.newArrayList(result);
        }
        return super.listByIds(idList);
    }


    /**
     * 查询函数
     * @param queryWrapper 查询参数
     * @param parameter 原始参数
     * @param paging 是否分页
     * @return 查询结果
     */
    @Override
    public List<? extends T> list(@NonNull QueryWrapper<T> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (!paging && org.apache.commons.lang3.StringUtils.isNotBlank(parameter.getSortName())) {
            this.analysisOrder(queryWrapper, parameter.getSortName(), parameter.getSortOrder());
        }
        final Page<?> page = PageCache.get();
        if (page != null) {
            CrudPageHelper.setPage(page);
        }
        return super.list(queryWrapper);
    }

    /**
     * 解析排序
     * @param queryWrapper 查询参数
     * @param sortName 排序字段
     * @param sortOrder 排序方向
     */
    public void analysisOrder(@NonNull QueryWrapper<T> queryWrapper, String sortName, String sortOrder) {
        final Class<? extends BaseModel> clazz = this.currentModelClass();
        final List<Sort> sortList = CrudUtils.analysisOrder(sortName, sortOrder, clazz);
        if (!sortList.isEmpty()) {
            sortList.forEach(sort -> {
                if (SORT_ASC.equalsIgnoreCase(sort.getOrder())) {
                    queryWrapper.orderByAsc(sort.getDbName());
                } else {
                    queryWrapper.orderByDesc(sort.getDbName());
                }
            });
        }
    }
}
