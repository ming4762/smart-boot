package com.smart.cloud.api.system.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.system.SysI18nApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;
import java.util.Map;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, contextId = "remoteSysI18nApi")
public interface RemoteSysI18nApi extends SysI18nApi {

    /**
     * 通过Locale 读取I18N信息
     *
     * @param locale Locale
     * @return 国际化信息
     */
    @Override
    @PostMapping(SystemApiUrlConstants.I18N_READ_BY_LOCALE)
    Map<String, String> readI18nByLocale(Locale locale);
}
