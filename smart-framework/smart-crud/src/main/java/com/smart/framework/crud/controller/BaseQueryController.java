package com.smart.framework.crud.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.framework.commons.core.constants.LabelValueEnum;
import com.smart.framework.commons.core.dto.common.LabelValueData;
import com.smart.framework.commons.core.message.PageData;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.utils.EnumUtils;
import com.smart.framework.crud.model.BaseModel;
import com.smart.framework.crud.model.Sort;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.query.ClassParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseService;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.framework.crud.utils.CrudPageHelper;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 基础查询controller
 * @author shizhongming
 * 2020/1/12 6:08 下午
 */
@Slf4j
public abstract class BaseQueryController<K extends BaseService<T>, T extends BaseModel> {

    protected final Class<T>[] typeArguments = (Class<T>[]) GenericTypeUtils.resolveTypeArguments(getClass(), BaseQueryController.class);

    @Getter
    protected final Class<T> entityClass = currentModelClass();

    protected K service;


    protected Class<T> currentModelClass() {
        return this.typeArguments[1];
    }

    /**
     * list查询方法
     * @param parameter 参数
     * @return 查询结果
     */
    public Result<Object> list(@NonNull PageSortQuery parameter) {
        return this.list(parameter, true);
    }

    /**
     * list查询方法
     * @param parameter 参数
     * @param isPickOmit 是否提取排除属性
     * @return 查询结果
     */
    public Result<Object> list(@NonNull PageSortQuery parameter, boolean isPickOmit) {
        final Page<T> page = this.doPage(parameter);
        List<?> data;
        if (page == null) {
            data = this.listData(parameter);
        } else {
            data = CrudPageHelper.withPage(page, () -> this.listData(parameter));
        }
        if (isPickOmit) {
            data = this.pickOmitByPropertyExclude(data, parameter);
        }
        if (page != null) {
            return Result.success(PageData.of(data, page.getTotal()));
        }
        return Result.success(data);
    }

    /**
     * 查询列表
     * @param parameter 参数
     * @return 查询结果
     */
    public List<T> listData(@NonNull PageSortQuery parameter) {
        final QueryWrapper<T> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter.getParameter(), this.getEntityClass());
        // 设置查询字段
        if (!parameter.getPropertyList().isEmpty()) {
            CrudUtils.setQueryField(parameter.getPropertyList(), this.getEntityClass(), queryWrapper);
        }
        // 排除的查询字典
        if (!CollectionUtils.isEmpty(parameter.getExcludePropertyList())) {
            queryWrapper.select(this.getEntityClass(), fieldInfo -> !parameter.getExcludePropertyList().contains(fieldInfo.getProperty()));
        }
        String keyword = parameter.getKeyword();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(keyword)) {
            this.addKeyword(queryWrapper, keyword);
        }
        return this.service.list(queryWrapper, parameter, CrudPageHelper.exists());
    }

    /**
     * 提取排除属性
     * 降低网络IO开销
     * @param dataList 需要提取的列表
     * @param parameter 分页参数
     * @return 提取后的列表
     */
    protected List<?> pickOmitByPropertyExclude(List<?> dataList, PageSortQuery parameter) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(parameter.getPropertyList()) && CollectionUtils.isEmpty(parameter.getExcludePropertyList())) {
            return dataList;
        }
        return dataList.stream().map(entity -> this.doPickOmit(entity, parameter)).toList();
    }

    /**
     * 提取排除属性
     * @param entity 实体类
     * @param parameter 参数
     * @return 提取后的map
     */
    private Map<String, Object> doPickOmit(Object entity, PageSortQuery parameter) {
        Map<String, Object> result = null;
        if (!CollectionUtils.isEmpty(parameter.getPropertyList())) {
            result = BeanUtil.beanToMap(entity, parameter.getPropertyList().toArray(new String[]{}));
        }
        if (!CollectionUtils.isEmpty(parameter.getExcludePropertyList())) {
            if (result == null) {
                result = BeanUtil.beanToMap(entity);
            }
            parameter.getExcludePropertyList().forEach(result::remove);
        }
        return result;
    }


    /**
     * 通过ID获取
     * @param id ID
     * @return 实体类
     */
    public Result<T> getById(@RequestBody Serializable id) {
        return Result.success(this.service.getById(id));
    }

    /**
     * 通过ID批量获取
     * @param ids ID列表
     * @return list
     */
    public Result<List<T>> listById(@RequestBody List<? extends Serializable> ids) {
        return Result.success(this.service.listByIds(ids));
    }


    /**
     * 查询枚举类 listEnumLabelValue
     * @param parameter 枚举类类型
     * @return label value
     */
    @SneakyThrows(ClassNotFoundException.class)
    public <M extends LabelValueEnum> Result<List<LabelValueData<M>>> listEnumLabelValue(@RequestBody @Valid ClassParameter parameter) {
        Class<M> aClass = (Class<M>) Class.forName(parameter.getClassName());
        if (!LabelValueEnum.class.isAssignableFrom(aClass)) {
            throw new IllegalArgumentException("类型错误，只能是com.smart.commons.core.com.smart.framework.tool.code.constants.LabelValueEnum的子类，并且是枚举类");
        }
        return Result.success(EnumUtils.convertLabelValue(aClass));
    }

    /**
     * 执行分页
     * @param parameter 参数信息
     * @return 分页信息
     */
    protected <P> Page<P> doPage(@NonNull PageSortQuery parameter) {
        return this.createPage(parameter.getPageSize(), parameter.getCurrentPage(), parameter.getSortName(), parameter.getSortOrder());
    }


    /**
     * 创建分页
     * @param pageSize 分页条数
     * @param currentPage 页数（优先级高）
     * @param sortName 排序字段
     * @param sortOrder 排序方向
     * @return 分页信息
     */
    @Nullable
    private <P> Page<P> createPage(@Nullable Integer pageSize, @Nullable Integer currentPage, @Nullable String sortName, @Nullable String sortOrder) {
        if (pageSize == null) {
            return null;
        }
        Page<P> page = Page.of(Objects.requireNonNullElse(currentPage, 1), pageSize);
        List<OrderItem> orderItemList = this.analysisOrder(sortName, sortOrder);
        if (Objects.nonNull(orderItemList)) {
            page.setOrders(orderItemList);
        }
        return page;
    }

    /**
     * 解析排序字段
     * @param sortName 排序名字
     * @param sortOrder 排序方向
     * @return 排序信息
     */
    @Nullable
    protected List<OrderItem> analysisOrder(@Nullable String sortName, @Nullable String sortOrder) {
        if (!StringUtils.hasLength(sortName)) {
            return null;
        }
        final List<Sort> sortList = CrudUtils.analysisOrder(sortName, sortOrder, this.getEntityClass());
        if (sortList.isEmpty()) {
            return null;
        }
        return sortList
                .stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setColumn(item.dbName());
                    orderItem.setAsc(item.asc());
                    return orderItem;
                }).toList();
    }


    /**
     * 添加关键字查询
     * @param queryWrapper 查询条件
     * @param keyword 关键字
     */
    private void addKeyword(@NonNull QueryWrapper<T> queryWrapper, @NonNull String keyword) {
        SmartTableInfo tableInfo = CrudUtils.getTableInfo(this.getEntityClass());
        queryWrapper.and(
                wrapper -> tableInfo.getFieldList()
                        .forEach(item -> wrapper.or().like(item.getColumn(), keyword))
        );
    }

    @Autowired
    public void setService(K service) {
        this.service = service;
    }
}
