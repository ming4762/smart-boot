package com.smart.db.generator.service.impl;

import com.smart.crud.service.BaseServiceImpl;
import com.smart.db.generator.mapper.DbCodeRelatedTableMapper;
import com.smart.db.generator.model.DbCodeRelatedTablePO;
import com.smart.db.generator.pojo.query.RelatedTableDeleteByMainConfigQuery;
import com.smart.db.generator.service.DbCodeRelatedTableService;
import org.springframework.stereotype.Service;

/**
 * @author shizhongming
 * 2021/5/12 9:20 下午
 */
@Service
public class DbCodeRelatedTableServiceImpl extends BaseServiceImpl<DbCodeRelatedTableMapper, DbCodeRelatedTablePO> implements DbCodeRelatedTableService {
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
