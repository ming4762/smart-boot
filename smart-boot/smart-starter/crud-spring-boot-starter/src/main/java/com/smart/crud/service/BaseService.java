package com.smart.crud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.crud.model.BaseModel;
import com.smart.crud.query.PageSortQuery;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 基础service层
 * @author shizhongming
 * 2020/1/10 9:50 下午
 */
public interface BaseService<T extends BaseModel> extends IService<T> {


    /**
     * 查询函数
     * @param queryWrapper 查询参数
     * @param parameter 原始参数
     * @param paging 是否分页
     * @return 查询结果
     */
    List<? extends T> list(@NonNull QueryWrapper<T> queryWrapper, @NonNull PageSortQuery parameter, boolean paging);

}
