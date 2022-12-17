package com.smart.crud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.crud.model.BaseModel;
import com.smart.crud.query.PageSortQuery;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 基础serivce层
 * @author shizhongming
 * 2020/1/10 9:50 下午
 */
public interface BaseService<T extends BaseModel> extends IService<T> {


    /**
     * 插入更新带有创建用户
     * @param model 实体
     * @param userId 用户ID
     * @return 是否成功
     * @deprecated com.smart.crud.mybatis.plugin.CreateUpdateUserTimeMybatisInterceptor
     */
    @Deprecated
    boolean saveOrUpdateWithCreateUser(@NonNull T model, Long userId);

    /**
     * 插入更新带有更新人员
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否成功
     * @deprecated com.smart.crud.mybatis.plugin.CreateUpdateUserTimeMybatisInterceptor
     */
    @Deprecated
    boolean saveOrUpdateWithUpdateUser(@NonNull T model, Long userId);

    /**
     * 插入更新带有所有人员
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否成功
     * @deprecated com.smart.crud.mybatis.plugin.CreateUpdateUserTimeMybatisInterceptor
     */
    @Deprecated
    boolean saveOrUpdateWithAllUser(@NonNull T model, Long userId);

    /**
     * 保存带有创建人员信息
     * @param model 实体类
     * @param userId 用户ID
     * @return 是否保存成功
     * @deprecated com.smart.crud.mybatis.plugin.CreateUpdateUserTimeMybatisInterceptor
     */
    @Deprecated
    boolean saveWithUser(@NonNull T model, Long userId);

    /**
     * 更新带有更新人员
     * @param model 实体类
     * @param userId 人员信息
     * @return 是否更新成功
     * @deprecated com.smart.crud.mybatis.plugin.CreateUpdateUserTimeMybatisInterceptor
     */
    @Deprecated
    boolean updateWithUserById(@NonNull T model, Long userId);

    /**
     * 查询函数
     * @param queryWrapper 查询参数
     * @param parameter 原始参数
     * @param paging 是否分页
     * @return 查询结果
     */
    List<? extends T> list(@NonNull QueryWrapper<T> queryWrapper, @NonNull PageSortQuery parameter, boolean paging);
    /**
     * 批量保存带有创建人员信息
     * @param modelList 实体类
     * @param userId 用户ID
     * @return 是否保存成功
     * @deprecated com.smart.crud.mybatis.plugin.CreateUpdateUserTimeMybatisInterceptor
     */
    @Deprecated
    boolean saveBatchWithUser(@NonNull List<T> modelList, Long userId);

    /**
     * 批量更新带有更新人员
     * @param modelList 实体类
     * @param userId 人员信息
     * @return 是否更新成功
     * @deprecated com.smart.crud.mybatis.plugin.CreateUpdateUserTimeMybatisInterceptor
     */
    @Deprecated
    boolean updateBatchWithUserById(@NonNull List<T> modelList, Long userId);

}
