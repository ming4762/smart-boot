package com.smart.module.code.service.impl;

import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.code.mapper.DbCodeRelatedTableMapper;
import com.smart.module.code.model.DbCodeRelatedTablePO;
import com.smart.module.code.pojo.query.RelatedTableDeleteByMainConfigQuery;
import com.smart.module.code.service.DbCodeRelatedTableService;
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
