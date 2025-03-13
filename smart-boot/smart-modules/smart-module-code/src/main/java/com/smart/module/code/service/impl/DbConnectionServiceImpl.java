package com.smart.module.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Sets;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.tool.database.executor.DatabaseExecutor;
import com.smart.framework.tool.database.executor.DbExecutorProvider;
import com.smart.framework.tool.database.pojo.bo.TableViewBO;
import com.smart.framework.tool.database.pool.model.DbConnectionConfig;
import com.smart.module.code.constants.DbCrudEnum;
import com.smart.module.code.mapper.DbConnectionMapper;
import com.smart.module.code.model.DbCodeConnectionUserGroupPO;
import com.smart.module.code.model.DbConnectionPO;
import com.smart.module.code.pojo.dto.DbConnectionSetUserGroupDTO;
import com.smart.module.code.pojo.dto.DbTableQueryDTO;
import com.smart.module.code.pojo.vo.connection.DbConnectionResultVO;
import com.smart.module.code.service.DbCodeConnectionUserGroupService;
import com.smart.module.code.service.DbConnectionService;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2021/4/25 16:35
 * @since 1.0
 */
@Service
public class DbConnectionServiceImpl extends BaseServiceImpl<DbConnectionMapper, DbConnectionPO> implements DbConnectionService {

    private final DbExecutorProvider dbExecutorProvider;

    private final DbCodeConnectionUserGroupService dbCodeConnectionUserGroupService;


    public DbConnectionServiceImpl(DbExecutorProvider dbExecutorProvider, DbCodeConnectionUserGroupService dbCodeConnectionUserGroupService) {
        this.dbExecutorProvider = dbExecutorProvider;
        this.dbCodeConnectionUserGroupService = dbCodeConnectionUserGroupService;
    }

    @Override
    public List<? extends DbConnectionPO> list(@NonNull QueryWrapper<DbConnectionPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (Objects.equals(parameter.getParameter().get(DbCrudEnum.LIST_BY_AUTH.name()), Boolean.TRUE)) {
            this.setAuthQuery(queryWrapper);
        }
        List<? extends DbConnectionPO> connectionList = super.list(queryWrapper, parameter, paging);
        // 创建VO类
        if (connectionList.isEmpty()) {
            return connectionList;
        }
        return connectionList.stream()
                .map(item -> {
                    DbConnectionResultVO vo = new DbConnectionResultVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).toList();
    }


    /**
     * 数据级过滤
     * @param queryWrapper 查询条件
     */
    private void setAuthQuery(@NonNull QueryWrapper<DbConnectionPO> queryWrapper) {
        if (AuthUtils.isSuperAdmin()) {
            return;
        }
        // 查询用户组对应的连接信息
        final Set<Long> connectionIds = Sets.newHashSet();

        queryWrapper.lambda().and(wrapper -> {
            wrapper.eq(DbConnectionPO :: getCreateUserId, AuthUtils.getNonNullCurrentUserId());
            if (!CollectionUtils.isEmpty(connectionIds)) {
                wrapper.or(query -> query.in(DbConnectionPO :: getId, connectionIds));
            }
        });
    }

    @Override
    public TableViewBO queryDbTable(DbTableQueryDTO parameter) {
        // 查询数据库连接信息
        final DbConnectionPO connection = this.getById(parameter.getDbConnectionId());
        if (connection == null) {
            return null;
        }
        final DbConnectionConfig connectionConfig = connection.createConnectionConfig();
        final DatabaseExecutor databaseExecutor = this.dbExecutorProvider.getDatabaseExecutor(connectionConfig);
        final List<TableViewBO> tableViewList = databaseExecutor.listTable(connectionConfig, parameter.getTableName());
        if (tableViewList.isEmpty()) {
            throw new BusinessException("未找到数据库表信息，请检查数据库表是否正确");
        }
        return tableViewList.getFirst();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setUserGroup(DbConnectionSetUserGroupDTO parameter) {
        // 删除数据
        this.dbCodeConnectionUserGroupService.remove(
                new QueryWrapper<DbCodeConnectionUserGroupPO>().lambda()
                .eq(DbCodeConnectionUserGroupPO :: getConnectionId, parameter.getConnectionId())
        );
        // 保存新数据
        List<DbCodeConnectionUserGroupPO> saveList = parameter.getUserGroupIdList().stream().map(item -> new DbCodeConnectionUserGroupPO(parameter.getConnectionId(), item))
                .toList();
        this.dbCodeConnectionUserGroupService.saveBatch(saveList);
        return true;
    }

    @SneakyThrows(SQLException.class)
    @Override
    public boolean testConnection(Long connectionId) {
        DbConnectionPO databaseConnection = this.getById(connectionId);
        if (databaseConnection == null) {
            throw new BusinessException("未找到数据库连接信息，请检查ID是否正确, id: " + connectionId);
        }
        DbConnectionConfig config = databaseConnection.createConnectionConfig();
        return this.dbExecutorProvider.getDatabaseExecutor(config).testConnection(config);
    }
}
