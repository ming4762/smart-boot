package com.smart.module.system.service.crud;

import com.smart.framework.crud.datapermission.model.SmartDataPermissionModel;
import com.smart.framework.crud.datapermission.provider.SmartDataPermissionProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据权限提供者
 * @author shizhongming
 * 2025/3/7 20:37
 * @since 5.0.0
 */
@Component
public class SmartDataPermissionProviderImpl implements SmartDataPermissionProvider {
    /**
     * 根据mapperId获取数据权限
     *
     * @param mapperId mapperId
     * @return 数据权限
     */
    @Override
    public List<SmartDataPermissionModel> getDataPermissionByMapper(String mapperId) {
        return List.of();
    }

    /**
     * 根据code获取数据权限
     *
     * @param codeList 数据权限编码
     * @return 数据权限
     */
    @Override
    public List<SmartDataPermissionModel> getDataPermissionByCode(List<String> codeList) {
        return List.of();
    }
}
