package com.smart.db.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.exception.BusinessException;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
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
import com.smart.system.service.SysUserGroupUserService;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final SysUserGroupUserService sysUserGroupUserService;

    private final UserSetterService createUpdateUserService;

    public DbConnectionServiceImpl(DbExecutorProvider dbExecutorProvider, DbCodeConnectionUserGroupService dbCodeConnectionUserGroupService, SysUserGroupUserService sysUserGroupUserService, UserSetterService createUpdateUserService) {
        this.dbExecutorProvider = dbExecutorProvider;
        this.dbCodeConnectionUserGroupService = dbCodeConnectionUserGroupService;
        this.sysUserGroupUserService = sysUserGroupUserService;
        this.createUpdateUserService = createUpdateUserService;
    }

    @SneakyThrows
    @Override
    public List<? extends DbConnectionPO> list(@NonNull QueryWrapper<DbConnectionPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (Objects.equals(parameter.getParameter().get(DbCrudEnum.LIST_BY_AUTH.name()), Boolean.TRUE)) {
            this.setAuthQuery(queryWrapper);
        }
        List<? extends DbConnectionPO> connectionList = super.list(queryWrapper, parameter, paging);
        // ??????VO???
        if (connectionList.isEmpty()) {
            return connectionList;
        }
        List<DbConnectionResultVO> connectionVoList = connectionList.stream()
                .map(item -> {
                    DbConnectionResultVO vo = new DbConnectionResultVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).collect(Collectors.toList());
        // ???????????????/?????????
        if (Objects.equals(parameter.getParameter().get(DbCrudEnum.QUERY_CREATE_UPDATE_USER.name()), Boolean.TRUE)) {
            this.queryCreateUpdateUser(connectionVoList);
        }
        return connectionVoList;
    }

    /**
     * ????????????????????????
     * @param connectionResultList VO List
     */
    private void queryCreateUpdateUser(List<DbConnectionResultVO> connectionResultList) {
        this.createUpdateUserService.setCreateUpdateUser(connectionResultList);
    }

    /**
     * ???????????????
     * @param queryWrapper ????????????
     */
    private void setAuthQuery(@NonNull QueryWrapper<DbConnectionPO> queryWrapper) {
        if (AuthUtils.isSuperAdmin()) {
            return;
        }
        // ????????????????????????????????????ID
        List<Long> groupIdList = this.sysUserGroupUserService.listGroupIdByUserId(Lists.newArrayList(AuthUtils.getNonNullCurrentUserId())).get(AuthUtils.getNonNullCurrentUserId());
        // ????????????????????????????????????
        final Set<Long> connectionIds = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(groupIdList)) {
            connectionIds.addAll(
                    this.dbCodeConnectionUserGroupService.list(
                            new QueryWrapper<DbCodeConnectionUserGroupPO>().lambda()
                                    .select(DbCodeConnectionUserGroupPO :: getConnectionId)
                                    .in(DbCodeConnectionUserGroupPO :: getUserGroupId, groupIdList)
                    ).stream().map(DbCodeConnectionUserGroupPO :: getConnectionId)
                            .filter(Objects :: nonNull)
                            .collect(Collectors.toSet())
            );
        }
        queryWrapper.lambda().and(wrapper -> {
            wrapper.eq(DbConnectionPO :: getCreateUserId, AuthUtils.getNonNullCurrentUserId());
            if (CollectionUtils.isNotEmpty(connectionIds)) {
                wrapper.or(query -> query.in(DbConnectionPO :: getId, connectionIds));
            }
        });
    }

    @Override
    public TableViewBO queryDbTable(DbTableQueryDTO parameter) {
        // ???????????????????????????
        final DbConnectionPO connection = this.getById(parameter.getDbConnectionId());
        if (connection == null) {
            return null;
        }
        final DbConnectionConfig connectionConfig = connection.createConnectionConfig();
        final DatabaseExecutor databaseExecutor = this.dbExecutorProvider.getDatabaseExecutor(connectionConfig);
        final List<TableViewBO> tableViewList = databaseExecutor.listTable(connectionConfig, parameter.getTableName());
        if (tableViewList.isEmpty()) {
            throw new BusinessException("???????????????????????????????????????????????????????????????");
        }
        return tableViewList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setUserGroup(DbConnectionSetUserGroupDTO parameter) {
        // ????????????
        this.dbCodeConnectionUserGroupService.remove(
                new QueryWrapper<DbCodeConnectionUserGroupPO>().lambda()
                .eq(DbCodeConnectionUserGroupPO :: getConnectionId, parameter.getConnectionId())
        );
        // ???????????????
        List<DbCodeConnectionUserGroupPO> saveList = parameter.getUserGroupIdList().stream().map(item -> new DbCodeConnectionUserGroupPO(parameter.getConnectionId(), item))
                .collect(Collectors.toList());
        this.dbCodeConnectionUserGroupService.saveBatchWithUser(saveList, AuthUtils.getNonNullCurrentUserId());
        return true;
    }

    @SneakyThrows
    @Override
    public boolean testConnection(Long connectionId) {
        DbConnectionPO databaseConnection = this.getById(connectionId);
        if (databaseConnection == null) {
            throw new BusinessException("??????????????????????????????????????????ID????????????, id: " + connectionId);
        }
        DbConnectionConfig config = databaseConnection.createConnectionConfig();
        return this.dbExecutorProvider.getDatabaseExecutor(config).testConnection(config);
    }
}
