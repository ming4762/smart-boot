package com.smart.system.api.remote;

import com.smart.module.api.system.SysParameterApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.system.api.local.LocalSysParameterApi;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 系统参数远程调用接口
 * @author zhongming4762
 * 2023/3/21
 */
@RestController
@RequestMapping
public class RemoteSysParameterApiController implements SysParameterApi {

    private final LocalSysParameterApi localSysParameterApi;

    public RemoteSysParameterApiController(LocalSysParameterApi localSysParameterApi) {
        this.localSysParameterApi = localSysParameterApi;
    }

    /**
     * 获取参数值
     *
     * @param code 系统参数编码
     * @return 系统参数值
     */
    @Nullable
    @Override
    @PostMapping(SystemApiUrlConstants.PARAMETER_GET)
    public String getParameter(@NonNull @RequestBody String code) {
        return this.localSysParameterApi.getParameter(code);
    }

    /**
     * 获取参数值
     *
     * @param codeList 系统参数编码
     * @return 系统参数值 Map
     */
    @NonNull
    @Override
    @PostMapping(SystemApiUrlConstants.PARAMETER_BATCH_GET)
    public Map<String, String> getParameter(@NonNull @RequestBody List<String> codeList) {
        return this.localSysParameterApi.getParameter(codeList);
    }
}
