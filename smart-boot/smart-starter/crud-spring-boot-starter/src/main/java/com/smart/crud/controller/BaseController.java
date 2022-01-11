package com.smart.crud.controller;

import com.smart.commons.core.message.Result;
import com.smart.crud.model.BaseModel;
import com.smart.crud.service.BaseService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 基础controller
 * @author shizhongming
 * 2020/1/12 8:35 下午
 */
public abstract class BaseController<K extends BaseService<T>, T extends BaseModel> extends BaseQueryController<K, T> {

    /**
     * 添加更新
     */
    public Result<Boolean> saveUpdate(@RequestBody T model) {
        return Result.success(this.service.saveOrUpdate(model));
    }

    /**
     * 保存
     */
    public Result<Boolean> save(@RequestBody T model) {
        return Result.success(this.service.save(model));
    }

    /**
     * 更新
     */
    public Result<Boolean> update(@RequestBody T model) {
        return Result.success(this.service.updateById(model));
    }


    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (idList.isEmpty()) {
            return Result.success(Boolean.FALSE);
        }
        return Result.success(this.service.removeByIds(idList));
    }

    /**
     * 批量保存
     */
    public Result<Boolean> batchSave(@RequestBody List<T> modelList) {
        return Result.success(this.service.saveBatch(modelList));
    }

    /**
     * 批量保存/更新
     */
    public Result<Boolean> batchSaveUpdate(@RequestBody List<T> modelList) {
        return Result.success(this.service.saveOrUpdateBatch(modelList));
    }

    /**
     * 获取request对象
     * @return request对象
     */
    @Nullable
    public HttpServletRequest getRequest() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    /**
     * 获取Response对象
     * @return Response对象
     */
    @Nullable
    public HttpServletResponse getResponse() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }
}
