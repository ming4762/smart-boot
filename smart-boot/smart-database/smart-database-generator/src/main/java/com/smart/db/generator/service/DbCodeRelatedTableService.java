package com.smart.db.generator.service;

import com.smart.crud.service.BaseService;
import com.smart.db.generator.model.DbCodeRelatedTablePO;
import com.smart.db.generator.pojo.query.RelatedTableDeleteByMainConfigQuery;

/**
 * @author shizhongming
 * 2021/5/12 9:19 下午
 */
public interface DbCodeRelatedTableService extends BaseService<DbCodeRelatedTablePO> {

    /**
     * 删除表关联信息
     * @param query 参数
     * @return 删除条数
     */
    Integer deleteByMainIdList(RelatedTableDeleteByMainConfigQuery query);
}
