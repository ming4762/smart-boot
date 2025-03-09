package com.smart.framework.crud.datapermission.provider;

import com.smart.framework.crud.datapermission.model.SmartDataPermissionModel;

import java.util.List;

/**
 * 数据权限提供者
 * @author shizhongming
 * 2025/3/5 19:48
 * @since 5.0.0
 */
public interface SmartDataPermissionProvider {

    /**
     * 根据mapperId获取数据权限
     * @param mapperId mapperId
     * @return 数据权限
     */
    List<SmartDataPermissionModel> getDataPermissionByMapper(String mapperId);

    /**
     * 根据code获取数据权限
     * @param codeList 数据权限编码
     * @return 数据权限
     */
    List<SmartDataPermissionModel> getDataPermissionByCode(List<String> codeList);
}
