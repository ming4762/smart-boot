package com.smart.crud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.smart.commons.core.exception.SystemException;
import com.smart.crud.mapper.CrudBaseMapper;
import com.smart.crud.model.BaseModel;
import com.smart.crud.model.Sort;
import com.smart.crud.parameter.SetUseYnParameter;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.utils.CrudPageHelper;
import com.smart.crud.utils.CrudUtils;
import com.smart.crud.utils.PageCache;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @author shizhongming
 * 2020/1/10 9:51 下午
 */
public abstract class BaseServiceImpl<K extends CrudBaseMapper<T>, T extends BaseModel> extends ServiceImpl<K, T> implements BaseService<T> {

    private static final String SORT_ASC = "ASC";

    private static final String COLUMN_USE_YN = "use_yn";

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
     * 设置启停状态
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    public boolean setUseYn(@NonNull SetUseYnParameter parameter) {
        if (CollectionUtils.isEmpty(parameter.getIdList())) {
            return false;
        }
        if (parameter.getUseYn() == null) {
            throw new SystemException("设置启停失败，启停状态不能为空");
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        Lists.partition(parameter.getIdList(), 500).forEach(list -> this.update(
                new UpdateWrapper<T>()
                        .set(COLUMN_USE_YN, parameter.getUseYn())
                        .in(tableInfo.getKeyColumn(), list)
        ));
        return true;
    }

    /**
     * 是否是添加操作
     * @param entity 实体
     * @return 是否是添加操作
     */
    protected boolean isAdd(@NonNull T entity) {
        TableInfo tableInfo = this.getTableInfo();
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
        return StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal));
    }

    /**
     * 获取表名
     * @return 表名
     */
    protected String getTableName() {
        TableInfo tableInfo = this.getTableInfo();
        return tableInfo.getTableName();
    }

    /**
     * 获取tableInfo
     * @return TableInfo
     */
    protected TableInfo getTableInfo() {
        return CrudUtils.getTableInfo(this.entityClass);
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
