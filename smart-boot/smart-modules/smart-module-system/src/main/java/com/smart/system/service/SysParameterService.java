package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysParameterPO;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
* sys_parameter - 系统参数表 Service
* @author SmartCodeGenerator
* 2023-2-27
*/
public interface SysParameterService extends BaseService<SysParameterPO> {

    /**
     * 获取参数值
     * @param code 系统参数编码
     * @return 系统参数值
     */
    @Nullable
    String getParameter(@NonNull String code);


    /**
     * 获取参数值
     * @param codeList 系统参数编码
     * @return 系统参数值 Map
     */
    @NonNull
    Map<String, String> getParameter(@NonNull List<String> codeList);

    /**
     * 添加更新
     * @param saveList 添加列表
     * @param updateList 更新列表
     * @return 是否更新成功
     */
    boolean saveUpdate(List<SysParameterPO> saveList, List<SysParameterPO> updateList);
}