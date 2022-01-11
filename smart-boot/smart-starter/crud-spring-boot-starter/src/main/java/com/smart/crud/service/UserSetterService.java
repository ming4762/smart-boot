package com.smart.crud.service;

import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.crud.model.CreateUserSetter;
import com.smart.crud.model.UpdateUserSetter;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/6/25 16:13
 * @since 1.0
 */
public interface UserSetterService {


    /**
     * 设置创建人
     * @param modelList 数据
     */
    void setCreateUser(List<? extends CreateUserSetter> modelList);

    /**
     * 设置更新人
     * @param modelList 数据
     */
    void setUpdateUser(List<? extends UpdateUserSetter> modelList);

    /**
     * 设置创建/更新人
     * @param modelList 数据
     */
    void setCreateUpdateUser(List<? extends CreateUpdateUserSetter> modelList);
}
