package com.smart.module.api.system;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 系统参数API
 * @author zhongming4762
 * 2023/3/20 21:15
 */
public interface SysParameterApi {

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
}
