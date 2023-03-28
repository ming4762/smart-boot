package com.smart.db.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Sets;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.exception.BusinessException;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.db.analysis.executor.DatabaseExecutor;
import com.smart.db.analysis.executor.DbExecutorProvider;
import com.smart.db.analysis.pojo.bo.TableViewBO;
import com.smart.db.analysis.pool.model.DbConnectionConfig;
import com.smart.db.generator.constants.DbCrudEnum;
import com.smart.db.generator.mapper.DbConnectionMapper;
import com.smart.db.generator.model.DbCodeConnectionUserGroupPO;
import com.smart.db.generator.model.DbConnectionPO;
import com.smart.db.generator.pojo.dto.DbConnectionSetUserGroupDTO;
import com.smart.db.generator.pojo.dto.DbTableQueryDTO;
import com.smart.db.generator.pojo.vo.connection.DbConnectionResultVO;
import com.smart.db.generator.service.DbCodeConnectionUserGroupService;
import com.smart.db.generator.service.DbConnectionService;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    @SneakyThrows
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
                }).collect(Collectors.toList());
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
        return tableViewList.get(0);
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
                .collect(Collectors.toList());
        this.dbCodeConnectionUserGroupService.saveBatch(saveList);
        return true;
    }

    @SneakyThrows
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
