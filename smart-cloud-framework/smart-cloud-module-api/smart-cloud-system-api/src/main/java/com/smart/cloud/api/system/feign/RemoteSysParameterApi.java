package com.smart.cloud.api.system.feign;

import com.smart.cloud.api.system.feign.fallback.RemoteSysParameterApiFallback;
import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.system.SysParameterApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * 系统参数远程调用接口
 * @author zhongming4762
 * 2023/3/21
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSysParameterApiFallback.class, contextId = "remoteSysParameterApi")
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
