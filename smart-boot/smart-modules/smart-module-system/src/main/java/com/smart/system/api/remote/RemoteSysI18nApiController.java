package com.smart.system.api.remote;

import com.smart.module.api.system.SysI18nApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.system.api.local.LocalSysI18nApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@RestController
@RequestMapping
public class RemoteSysI18nApiController implements SysI18nApi {

    private final LocalSysI18nApi localSysI18nApi;

    public RemoteSysI18nApiController(LocalSysI18nApi localSysI18nApi) {
        this.localSysI18nApi = localSysI18nApi;
    }

    /**
     * 通过Locale 读取I18N信息
     *
     * @param locale Locale
     * @return 国际化信息
     */
    @Override
    @PostMapping(SystemApiUrlConstants.I18N_READ_BY_LOCALE)
    public Map<String, String> readI18nByLocale(@RequestBody Locale locale) {
        return this.localSysI18nApi.readI18nByLocale(locale);
    }
}
