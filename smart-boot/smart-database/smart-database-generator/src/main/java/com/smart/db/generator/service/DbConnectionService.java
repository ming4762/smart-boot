package com.smart.db.generator.service;


import com.smart.crud.service.BaseService;
import com.smart.db.analysis.pojo.bo.TableViewBO;
import com.smart.db.generator.model.DbConnectionPO;
import com.smart.db.generator.pojo.dto.DbConnectionSetUserGroupDTO;
import com.smart.db.generator.pojo.dto.DbTableQueryDTO;

/**
 * @author ShiZhongMing
 * 2021/4/25 16:35
 * @since 1.0
 */
public interface DbConnectionService extends BaseService<DbConnectionPO> {

    /**
     * 查询数据库表信息
     * @param parameter 参数
     * @return TableViewBO
     */
    TableViewBO queryDbTable(DbTableQueryDTO parameter);

    /**
     * 数据库连接设置用户组
     * @param parameter 参数
     * @return 是否成功
     */
    boolean setUserGroup(DbConnectionSetUserGroupDTO parameter);

    /**
     * 测试数据库连接
     * @param connectionId 数据库连接ID
     * @return 测试结果
     */
    boolean testConnection(Long connectionId);
}
