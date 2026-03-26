package com.smart.module.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.code.constants.ButtonIdentEnum;
import com.smart.module.code.constants.ButtonListEnum;
import com.smart.module.code.mapper.DbCodeButtonConfigMapper;
import com.smart.module.code.model.DbCodeButtonConfigPO;
import com.smart.module.code.service.DbCodeButtonConfigService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2021/5/11 9:27 下午
 */
@Service
public class DbCodeButtonConfigServiceImpl extends BaseServiceImpl<DbCodeButtonConfigMapper, DbCodeButtonConfigPO> implements DbCodeButtonConfigService {
    @Override
    @NonNull
    public Map<ButtonIdentEnum, List<ButtonListEnum>> queryButtonMapByRelatedId(@NonNull Long relatedId) {
        return this.list(
                new QueryWrapper<DbCodeButtonConfigPO>().lambda()
                .eq(DbCodeButtonConfigPO :: getRelatedId, relatedId)
                .orderByAsc(DbCodeButtonConfigPO :: getSeq)
        ).stream().collect(
                Collectors.groupingBy(
                        DbCodeButtonConfigPO :: getIdent, Collectors.mapping(item -> ButtonListEnum.valueOf(item.getButton()), Collectors.toList())
                )
        );
    }
}
