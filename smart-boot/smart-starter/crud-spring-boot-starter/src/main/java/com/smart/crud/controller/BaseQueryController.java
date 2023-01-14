package com.smart.crud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.smart.commons.core.message.PageData;
import com.smart.commons.core.message.Result;
import com.smart.crud.model.BaseModel;
import com.smart.crud.model.Sort;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseService;
import com.smart.crud.utils.CrudUtils;
import com.smart.crud.utils.PageCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基础查询controller
 * @author shizhongming
 * 2020/1/12 6:08 下午
 */
@Slf4j
public abstract class BaseQueryController<K extends BaseService<T>, T extends BaseModel> {

    @Autowired
    protected K service;


    /**
     * list查询方法
     * @param parameter 参数
     * @return 查询结果
     */
    public Result<Object> list(@NonNull PageSortQuery parameter) {
        final Page<T> page = this.doPage(parameter);
        PageCache.set(page);
        final QueryWrapper<T> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter.getParameter(), this.getModelType());
        // 设置查询字段
        if (!parameter.getPropertyList().isEmpty()) {
            CrudUtils.setQueryField(parameter.getPropertyList(), this.getModelType(), queryWrapper);
        }
        String keyword = parameter.getKeyword();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(keyword)) {
            this.addKeyword(queryWrapper, keyword);
        }
        final List<? extends T> data = this.service.list(queryWrapper, parameter, page != null);
        if (page != null) {
            return Result.success(new PageData<>(data, page.getTotal()));
        }
        return Result.success(data);
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
     * 执行分页
     * @param parameter 参数信息
     * @return 分页信息
     */
    protected Page<T> doPage(@NonNull PageSortQuery parameter) {
        return this.createPage(parameter.getLimit(), parameter.getOffset(), parameter.getPage(), parameter.getSortName(), parameter.getSortOrder());
    }


    /**
     * 创建分页
     * @param limit 分页条数
     * @param offset 开始记录
     * @param pageNum 页数（优先级高）
     * @param sortName 排序字段
     * @param sortOrder 排序方向
     * @return 分页信息
     */
    @Nullable
    private Page<T> createPage(@Nullable Integer limit, @Nullable Integer offset, @Nullable Integer pageNum, @Nullable String sortName, @Nullable String sortOrder) {
        Page<T> page = null;
        if (Objects.nonNull(limit)) {
            // 解析排序字段
            final String orderMessage = this.analysisOrder(sortName, sortOrder);
            // 进行分页
            if (Objects.nonNull(pageNum)) {
                page = new Page<>(pageNum, limit, true);
            } else {
                if (ObjectUtils.isEmpty(offset)) {
                    offset = 0;
                }
                page = new Page<>(new int[]{offset, limit}, true);
            }
            if (StringUtils.hasLength(orderMessage)) {
                page.setOrderBy(orderMessage);
            }
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
    protected String analysisOrder(@Nullable String sortName, @Nullable String sortOrder) {
        if (StringUtils.hasLength(sortName)) {
            final Class<? extends BaseModel> clazz = CrudUtils.getModelClassByType(this.getModelType());
            final List<Sort> sortList = CrudUtils.analysisOrder(sortName, sortOrder, clazz);
            if (sortList.isEmpty()) {
                return null;
            }
            return sortList
                    .stream()
                    .map(sort -> String.format("%s %s", sort.getDbName(), sort.getOrder()))
                    .collect(Collectors.joining(","));
        }
        return null;
    }


    /**
     * 获取实体类类型
     * @return 实体类类型
     */
    private Type getModelType() {
        return ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * 添加关键字查询
     * @param queryWrapper 查询条件
     * @param keyword 关键字
     */
    private void addKeyword(@NonNull QueryWrapper<T> queryWrapper, @NonNull String keyword) {
        final Class<? extends BaseModel>  clazz = CrudUtils.getModelClassByType(getModelType());
        if (clazz != null) {
            final Field[] fieldList = clazz.getDeclaredFields();
            if (fieldList.length > 0) {
                queryWrapper.and(wrapper -> Arrays.asList(fieldList)
                        .forEach(field -> wrapper.or().like(CrudUtils.getDbField(field), keyword)));
            }
        }
    }
}
