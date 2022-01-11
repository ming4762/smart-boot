package com.smart.db.generator.service;

import com.smart.crud.service.BaseService;
import com.smart.db.generator.constants.ButtonIdentEnum;
import com.smart.db.generator.constants.ButtonListEnum;
import com.smart.db.generator.model.DbCodeButtonConfigPO;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @author shizhongming
 * 2021/5/11 9:27 下午
 */
public interface DbCodeButtonConfigService extends BaseService<DbCodeButtonConfigPO> {

    /**
     * 通过关联类型ID获取button类型 map
     * @param relatedId 关联ID
     * @return result
     */
    @NonNull
    Map<ButtonIdentEnum, List<ButtonListEnum>> queryButtonMapByRelatedId(@NonNull Long relatedId);
}
