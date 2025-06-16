package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.crud.datapermission.handler.SmartDataPermissionController;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.constants.SysI18nPlatformEnum;
import com.smart.module.system.mapper.SysI18nJsonMapper;
import com.smart.module.system.model.SysI18nJsonItemPO;
import com.smart.module.system.model.SysI18nJsonPO;
import com.smart.module.system.service.SysI18nJsonItemService;
import com.smart.module.system.service.SysI18nJsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
* sys_i18n_json - 国际化信息json Service实现类
* @author SmartCodeGenerator
* 2025年1月3日 14:39:00
*/
@Service
@RequiredArgsConstructor
public class SysI18nJsonServiceImpl extends BaseServiceImpl<SysI18nJsonMapper, SysI18nJsonPO> implements SysI18nJsonService {

    private final SysI18nJsonItemService sysI18nJsonItemService;

    /**
     * 重写批量删除方法，如果ID只有一个调用removeById方法
     *
     * @param idList ID列表
     * @return 删除结果
     */
    @Override
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 删除子表数据
        this.sysI18nJsonItemService.remove(
                new LambdaQueryWrapper<SysI18nJsonItemPO>()
                        .in(SysI18nJsonItemPO::getHeadId, idList)
        );
        return super.removeByIds(idList);
    }

    /**
     * 读取国际化信息
     * @param locale 语言
     * @param platform 平台
     * @return 国际化信息
     */
    @Override
    public JsonNode readToJsonByLocale(Locale locale, SysI18nPlatformEnum platform) {
        // 忽略数据权限
        SmartDataPermissionController.ignoreAll();

        LambdaQueryWrapper<SysI18nJsonPO> queryWrapper = new LambdaQueryWrapper<SysI18nJsonPO>()
                .select(SysI18nJsonPO::getId)
                .eq(SysI18nJsonPO::getUseYn, true);
        if (platform != null) {
            queryWrapper.eq(SysI18nJsonPO::getPlatform, platform);
        }
        List<SysI18nJsonPO> headList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(headList)) {
            return null;
        }
        List<String> itemList = this.sysI18nJsonItemService.lambdaQuery()
                .in(SysI18nJsonItemPO::getHeadId, headList.stream().map(SysI18nJsonPO::getId).toList())
                .eq(SysI18nJsonItemPO::getLocale, locale.toLanguageTag())
                .eq(SysI18nJsonItemPO::getUseYn, true)
                .list().stream()
                .map(SysI18nJsonItemPO::getData)
                .filter(StringUtils::hasText)
                .toList();
        if (CollectionUtils.isEmpty(itemList)) {
            return null;
        }
        return JsonUtils.deepMerge(itemList.toArray(new String[0]));
    }
}