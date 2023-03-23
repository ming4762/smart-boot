package com.smart.system.api.local;

import com.smart.module.api.system.SysParameterApi;
import com.smart.system.service.SysParameterService;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author zhongming4762
 * 2023/3/20 21:17
 */
@Component
@Primary
public class LocalSysParameterApi implements SysParameterApi {

    private final SysParameterService sysParameterService;

    public LocalSysParameterApi(SysParameterService sysParameterService) {
        this.sysParameterService = sysParameterService;
    }

    /**
     * 获取参数值
     *
     * @param code 系统参数编码
     * @return 系统参数值
     */
    @Nullable
    @Override
    public String getParameter(@NonNull String code) {
        return this.sysParameterService.getParameter(code);
    }

    /**
     * 获取参数值
     *
     * @param codeList 系统参数编码
     * @return 系统参数值 Map
     */
    @NonNull
    @Override
    public Map<String, String> getParameter(@NonNull List<String> codeList) {
        return this.sysParameterService.getParameter(codeList);
    }
}
