package com.smart.crud.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.smart.commons.core.utils.IdGenerator;
import com.smart.crud.constants.UserPropertyEnum;
import com.smart.crud.mapper.CrudBaseMapper;
import com.smart.crud.model.BaseModel;
import com.smart.crud.model.Sort;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.utils.CrudPageHelper;
import com.smart.crud.utils.CrudUtils;
import com.smart.crud.utils.PageCache;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author shizhongming
 * 2020/1/10 9:51 下午
 */
public abstract class BaseServiceImpl<K extends CrudBaseMapper<T>, T extends BaseModel> extends ServiceImpl<K, T> implements BaseService<T> {

    private static final String SORT_ASC = "ASC";

    private static final String KEY_PROPERTY_NULL_ERROR = "error: can not execute. because can not find column for id from entity!";


    @Override
    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    @Override
    protected Class<T> currentMapperClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }

    /**
     * 重写批量删除方法，如果ID只有一个调用removeById方法
     * @param idList ID列表
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (idList.size() == 1) {
            return this.removeById(idList.iterator().next());
        }
        return super.removeByIds(idList);
    }


    /**
     * 重写save方法，修改ID的生成策略
     * @author jackson
     * @param entity 实体类
     * @return 是否保存成功
     */
    @Override
    public boolean save(@NonNull T entity) {
        // 获取key
        final TableInfo tableInfo = this.getTableInfo();
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, KEY_PROPERTY_NULL_ERROR);
        Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
        if (StringUtils.checkValNull(idVal)) {
            // 如果ID为null 手动设置ID
            this.setNumberId(entity, tableInfo);
        }
        return super.save(entity);
    }

    /**
     * 重写批量save方法，修改ID的生成策略
     * @author jackson
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        if (ObjectUtils.isEmpty(entityList)) {
            return false;
        }
        // 获取实体类tableInfo
        final TableInfo tableInfo = this.getTableInfo();
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, KEY_PROPERTY_NULL_ERROR);
        // 遍历实体类设置主键
        entityList.forEach(entity -> {
            Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
            if (StringUtils.checkValNull(idVal)) {
                // 如果ID为null 手动设置ID
                this.setNumberId(entity, tableInfo);
            }
        });
        return super.saveBatch(entityList, batchSize);
    }

    /**
     * 获取 TableInfo
     * @return 实体类TableInfo信息
     */
    @NonNull
    protected TableInfo getTableInfo() {
        final TableInfo tableInfo = TableInfoHelper.getTableInfo(this.currentModelClass());
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        return tableInfo;
    }


    /**
     * 设置number类型的ID
     * 修改主键生成策略
     * @param entity 实体类
     * @param tableInfo 表信息
     */
    @SneakyThrows
    protected void setNumberId(@NonNull T entity, @NonNull TableInfo tableInfo) {
        final IdType idType = tableInfo.getIdType();
        if (idType.getKey() == IdType.ASSIGN_ID.getKey() && Number.class.isAssignableFrom(tableInfo.getKeyType())) {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(tableInfo.getKeyProperty(), entity.getClass());
            propertyDescriptor.getWriteMethod().invoke(entity, IdGenerator.nextId());

        }
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
     * 插入更新带有创建用户
     * @param model 实体
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateWithCreateUser(@NonNull T model, Long userId) {
        boolean isAdd = this.isAdd(model);
        if (isAdd) {
            return this.saveWithUser(model, userId);
        } else {
            return this.updateById(model);
        }
    }

    /**
     * 插入更新带有更新人员
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateWithUpdateUser(@NonNull T model, Long userId) {
        boolean isAdd = this.isAdd(model);
        if (isAdd) {
            return this.save(model);
        } else {
            return this.updateWithUserById(model, userId);
        }
    }

    /**
     * 插入更新带有所有人员
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateWithAllUser(@NonNull T model, Long userId) {
        boolean isAdd = this.isAdd(model);
        if (isAdd) {
            return this.saveWithUser(model, userId);
        } else {
            return this.updateWithUserById(model, userId);
        }
    }

    /**
     * 保存带有创建人员信息
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithUser(@NonNull T model, Long userId) {
        this.setCreateUserId(model, userId);
        this.setCreateTime(model);
        return this.save(model);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWithUserById(@NonNull T model, Long userId) {
        this.setUpdateTime(model);
        this.setUpdateUserId(model, userId);
        return updateById(model);
    }

    /**
     * 批量保存带有创建人员信息
     * @param modelList 实体类
     * @param userId 用户ID
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchWithUser(@NonNull List<T> modelList, Long userId) {
        if (CollectionUtils.isNotEmpty(modelList)) {
            modelList.forEach(item -> {
                this.setCreateUserId(item, userId);
                this.setCreateTime(item);
            });
            return this.saveBatch(modelList);
        }
        return false;
    }

    /**
     * 批量更新带有更新人员
     * @param modelList 实体类
     * @param userId 人员信息
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchWithUserById(@NonNull List<T> modelList, Long userId) {
        if (CollectionUtils.isNotEmpty(modelList)) {
            modelList.forEach(item -> {
                this.setUpdateTime(item);
                this.setUpdateUserId(item, userId);
            });
            return this.updateBatchById(modelList);
        }
        return false;
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


    /**
     * 判断是否是保存操作
     * @param entity 实体类
     * @return 是否执行保存
     */
    protected boolean isAdd(@NonNull T entity) {
        Serializable key = this.getKeyValue(entity);
        return StringUtils.checkValNull(key) || Objects.isNull(this.getById(key));
    }

    /**
     * 设置创建用户ID
     * @param model 实体类
     * @param userId 用户ID
     */
    @SneakyThrows
    protected void setCreateUserId(T model, Long userId) {
        PropertyUtils.setProperty(model, UserPropertyEnum.CREATE_USER_ID.getName(), userId);
    }

    /**
     * 设置创建时间
     * @param model 实体类
     */
    @SneakyThrows
    protected void setCreateTime(T model) {
        PropertyUtils.setProperty(model, UserPropertyEnum.CREATE_TIME.getName(), LocalDateTime.now());
    }

    @SneakyThrows
    protected void setUpdateUserId(T model, Long userId) {
        PropertyUtils.setProperty(model, UserPropertyEnum.UPDATE_USER_ID.getName(), userId);
    }

    @SneakyThrows
    protected void setUpdateTime(T model) {
        PropertyUtils.setProperty(model, UserPropertyEnum.UPDATE_TIME.getName(), LocalDateTime.now());
    }

    /**
     * 获取主键的值
     * @param entity 实体类
     * @return key
     */
    protected Serializable getKeyValue(@NonNull T entity) {
        Class<?> cls = entity.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, KEY_PROPERTY_NULL_ERROR);
        return (Serializable) ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
    }
}
