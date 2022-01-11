package com.smart.db.generator.service.impl;

import com.smart.crud.service.BaseServiceImpl;
import com.smart.db.generator.mapper.DbCodeRuleConfigMapper;
import com.smart.db.generator.model.DbCodeRuleConfigPO;
import com.smart.db.generator.pojo.query.RelatedTableDeleteByMainConfigQuery;
import com.smart.db.generator.service.DbCodeRuleConfigService;
import org.springframework.stereotype.Service;

/**
 * @author ShiZhongMing
 * 2021/6/11 8:39
 * @since 1.0
 */
@Service
public class DbCodeRuleConfigServiceImpl extends BaseServiceImpl<DbCodeRuleConfigMapper, DbCodeRuleConfigPO> implements DbCodeRuleConfigService {

    /**
     * 删除表关联信息
     * @param query 参数
     * @return 删除条数
     */
    @Override
    public Integer deleteByMainIdList(RelatedTableDeleteByMainConfigQuery query) {
        return this.baseMapper.deleteByMainIdList(query);
    }
}
