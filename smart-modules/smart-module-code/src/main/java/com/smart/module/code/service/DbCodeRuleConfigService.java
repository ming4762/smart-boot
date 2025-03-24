package com.smart.module.code.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.code.model.DbCodeRuleConfigPO;
import com.smart.module.code.pojo.query.RelatedTableDeleteByMainConfigQuery;

/**
 * @author ShiZhongMing
 * 2021/6/11 8:39
 * @since 1.0
 */
public interface DbCodeRuleConfigService extends BaseService<DbCodeRuleConfigPO> {

    /**
     * 删除表关联信息
     * @param query 参数
     * @return 删除条数
     */
    Integer deleteByMainIdList(RelatedTableDeleteByMainConfigQuery query);
}
