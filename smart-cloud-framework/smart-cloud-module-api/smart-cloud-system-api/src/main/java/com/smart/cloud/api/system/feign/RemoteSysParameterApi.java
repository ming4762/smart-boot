package com.smart.cloud.api.system.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.system.SysParameterApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * 系统参数远程调用接口
 * @author zhongming4762
 * 2023/3/21
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, contextId = "remoteSysParameterApi")
public interface RemoteSysParameterApi extends SysParameterApi {

    /**
     * 获取参数值
     *
     * @param code 系统参数编码
     * @return 系统参数值
     */
    @Nullable
    @Override
    @PostMapping(SystemApiUrlConstants.PARAMETER_GET)
    String getParameter(@NonNull String code);

    /**
     * 获取参数值
     *
     * @param codeList 系统参数编码
     * @return 系统参数值 Map
     */
    @NonNull
    @Override
    @PostMapping(SystemApiUrlConstants.PARAMETER_BATCH_GET)
    Map<String, String> getParameter(@NonNull List<String> codeList);
}
