package com.smart.module.code.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.framework.tool.database.pojo.bo.TableViewBO;
import com.smart.module.code.model.DbConnectionPO;
import com.smart.module.code.pojo.dto.DbConnectionSetUserGroupDTO;
import com.smart.module.code.pojo.dto.DbTableQueryDTO;

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
