package com.smart.module.code.mapper;

import com.smart.framework.crud.mapper.CrudBaseMapper;
import com.smart.module.code.model.DbCodeRuleConfigPO;
import com.smart.module.code.pojo.query.RelatedTableDeleteByMainConfigQuery;

/**
 * @author ShiZhongMing
 * 2021/6/11 8:39
 * @since 1.0
 */
public interface DbCodeRuleConfigMapper extends CrudBaseMapper<DbCodeRuleConfigPO> {

    /**
     * 通过主表ID删除配置信息
     * @param query 查询条件
     * @return 删除记录数
     */
    Integer deleteByMainIdList(RelatedTableDeleteByMainConfigQuery query);
}
