package com.smart.module.system.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.smart.framework.commons.core.message.Result;
import com.smart.module.system.constants.SysI18nPlatformEnum;
import com.smart.module.system.pojo.parameter.i18n.SysI18nReadParameter;
import com.smart.module.system.service.SysI18nJsonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

/**
 * @author shizhongming
 * 2025/1/4 16:26
 * @since 5.0.0
 */
@RestController
@RequestMapping("public")
@RequiredArgsConstructor
public class PublicController {

    private final SysI18nJsonService sysI18nJsonService;

    @Operation(summary = "读取前台国际化信息")
    @PostMapping("i18n/readFrontI18n")
    public Result<JsonNode> readFrontI18n(@RequestBody SysI18nReadParameter parameter) {
        Locale locale = LocaleContextHolder.getLocale();
        if (StringUtils.hasText(parameter.getLocale())) {
            locale = Locale.forLanguageTag(parameter.getLocale());
        }
        return Result.success(this.sysI18nJsonService.readToJsonByLocale(locale, SysI18nPlatformEnum.FRONT));
    }
}
