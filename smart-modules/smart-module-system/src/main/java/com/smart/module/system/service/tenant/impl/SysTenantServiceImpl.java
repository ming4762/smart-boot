package com.smart.module.system.service.tenant.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.commons.core.utils.SmartIdGenerator;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.query.IdParameter;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.system.inject.SysTenantInject;
import com.smart.module.system.mapper.tenant.SysTenantMapper;
import com.smart.module.system.mapper.tenant.SysTenantUserMapper;
import com.smart.module.system.model.SysRolePO;
import com.smart.module.system.model.SysUserAccountPO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.model.SysUserRolePO;
import com.smart.module.system.model.tenant.SysTenantPO;
import com.smart.module.system.model.tenant.SysTenantPackagePO;
import com.smart.module.system.model.tenant.SysTenantSubscribePO;
import com.smart.module.system.model.tenant.SysTenantUserPO;
import com.smart.module.system.pojo.dbo.tenant.SysTenantUserListDO;
import com.smart.module.system.pojo.dto.tenant.*;
import com.smart.module.system.service.SysRoleService;
import com.smart.module.system.service.SysUserAccountService;
import com.smart.module.system.service.SysUserRoleService;
import com.smart.module.system.service.SysUserService;
import com.smart.module.system.service.tenant.SysTenantPackageService;
import com.smart.module.system.service.tenant.SysTenantService;
import com.smart.module.system.service.tenant.SysTenantSubscribeService;
import com.smart.module.system.service.tenant.SysTenantUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
* sys_tenant - 租户表 Service实现类
* @author SmartCodeGenerator
* 2024年3月29日 上午10:42:40
*/
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl extends BaseServiceImpl<SysTenantMapper, SysTenantPO> implements SysTenantService {

    private static final String DEFAULT_ROLE_CODE = "ADMIN";
    private static final String DEFAULT_ROLE_NAME = "管理员";
    private static final Long ADMIN_USER_ID = 1L;

    private final SysTenantUserMapper sysTenantUserMapper;
    private final SysUserService sysUserService;
    private final SysTenantUserService sysTenantUserService;
    private final SysTenantPackageService sysTenantPackageService;
    private final SysTenantSubscribeService sysTenantSubscribeService;
    private final SysRoleService sysRoleService;
    private final SysUserAccountService sysUserAccountService;
    private final SysUserRoleService sysUserRoleService;

    /**
     * 查询租户对应用户
     *
     * @param parameter 参数
     * @return 用户列表
     */
    @Override
    public List<SysTenantUserListDO> listTenantUser(SysTenantUserListDTO parameter) {
        SmartTableInfo tableInfo = CrudUtils.getTableInfo(SysTenantUserPO.class);
        SmartTableInfo userTableInfo = CrudUtils.getTableInfo(SysUserPO.class);
        QueryWrapper<SysTenantUserPO> queryWrapper = new QueryWrapper<>();
        if (parameter.getFullName() != null) {
            queryWrapper.like(userTableInfo.getTableFiled(SysUserPO::getFullName).getColumn(), parameter.getFullName());
        }
        if (parameter.getUsername() != null) {
            queryWrapper.like(userTableInfo.getTableFiled(SysUserPO::getUsername).getColumn(), parameter.getUsername());
        }
        TableFieldInfo tableFiled = tableInfo.getTableFiled(SysTenantUserPO::getTenantId);
        queryWrapper.eq("B." + tableFiled.getColumn(), parameter.getTenantId());
        return this.sysTenantUserMapper.listTenantUser(queryWrapper);
    }

    /**
     * 查询未绑定租户的用户
     *
     * @param parameter 参数
     * @return 用户列表
     */
    @Override
    public List<SysUserPO> listNoBindUser(SysTenantListNoBindUserDTO parameter) {
        QueryWrapper<SysUserPO> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter.getParameter(), SysUserPO.class);
        queryWrapper.apply("user_id not in (select M.user_id from sys_tenant_user M where M.tenant_id = {0})", parameter.getTenantId());
        return this.sysUserService.list(queryWrapper);
    }

    /**
     * 绑定用户
     *
     * @param parameter 参数
     * @return 是否绑定成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindTenantUser(SysTenantBindUserDTO parameter) {
        if (CollectionUtils.isEmpty(parameter.getUserIdList())) {
            return false;
        }
        List<SysTenantUserPO> modelList = parameter.getUserIdList().stream()
                .map(userId -> {
                    SysTenantUserPO tenantUser = new SysTenantUserPO();
                    tenantUser.setTenantId(parameter.getTenantId());
                    tenantUser.setUserId(userId);
                    return tenantUser;
                }).toList();
        boolean result = this.sysTenantUserService.saveBatch(modelList);
        // 创建账户
        if (!Boolean.TRUE.equals(parameter.getCreateAccount())) {
            return result;
        }
        if (!AuthUtils.isPlatformTenant()) {
            throw new AccessDeniedException("非平台管理租户无权限创建其他租户账户");
        }
        this.sysUserAccountService.createAccount(parameter.getTenantId(), parameter.getUserIdList());
        return result;
    }

    /**
     * 解绑用户
     *
     * @param parameter 参数
     * @return 是否解绑成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBindUser(SysTenantRemoveBindUserDTO parameter) {
        Long tenantId = Objects.requireNonNullElseGet(parameter.getTenantId(), AuthUtils::getNonNullCurrentTenantId);
        // 删除用户账户信息
        this.sysUserAccountService.remove(
                new LambdaQueryWrapper<>(SysUserAccountPO.class)
                      .in(SysUserAccountPO::getUserId, parameter.getUserIdList())
                        .eq(SysUserAccountPO::getTenantId, tenantId)
        );
        return this.sysTenantUserService.remove(
                new LambdaQueryWrapper<>(SysTenantUserPO.class)
                        .eq(SysTenantUserPO::getTenantId, tenantId)
                        .in(SysTenantUserPO::getUserId, parameter.getUserIdList())
        );
    }

    /**
     * 根据租户ID查询没有绑定的套餐
     *
     * @param parameter 参数
     * @return 套餐包列表
     */
    @Override
    public List<SysTenantPackagePO> listNoBindPackageByTenantId(IdParameter parameter) {
        LambdaQueryWrapper<SysTenantPackagePO> queryWrapper = new LambdaQueryWrapper<>(SysTenantPackagePO.class)
                .apply("id not in (select A.package_id from sys_tenant_subscribe A where A.tenant_id = {0})", parameter.getId());
        return this.sysTenantPackageService.list(queryWrapper);
    }

    /**
     * 重写批量删除方法，如果ID只有一个调用removeById方法
     *
     * @param idList ID列表
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        long count = this.count(
                new LambdaQueryWrapper<>(SysTenantPO.class)
                        .in(SysTenantPO::getId, idList)
                        .eq(SysTenantPO::getPlatformYn, Boolean.TRUE)
        );
        if (count > 0) {
            throw new BusinessException("不能删除平台管理租户");
        }
        // 删除租户用户关联关系表
        this.sysTenantUserService.remove(
                new LambdaQueryWrapper<>(SysTenantUserPO.class)
                        .in(SysTenantUserPO::getTenantId, idList)
        );
        // 删除租户订阅
        this.sysTenantSubscribeService.remove(
                new LambdaQueryWrapper<>(SysTenantSubscribePO.class)
                        .in(SysTenantSubscribePO::getTenantId, idList)
        );
        return super.removeByIds(idList);
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<SysTenantPO> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        Set<Long> idList = entityList.stream()
                .map(SysTenantPO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(idList)) {
            long count = this.count(
                    new LambdaQueryWrapper<>(SysTenantPO.class)
                            .in(SysTenantPO::getId, idList)
                            .eq(SysTenantPO::getPlatformYn, Boolean.TRUE)
            );
            if (count > 0) {
                throw new BusinessException("不能修改平台管理租户");
            }
        }
        return super.saveOrUpdateBatch(entityList);
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Override
    public boolean saveOrUpdate(SysTenantPO entity) {
        boolean isAdd = true;
        if (entity.getId() != null) {
            SysTenantPO old = this.getById(entity.getId());
            if (old != null) {
                if (Boolean.TRUE.equals(old.getPlatformYn())) {
                    throw new BusinessException("不能修改平台管理租户");
                }
                isAdd = false;
            }
        }
        if (!isAdd) {
            return this.updateById(entity);
        }
        long newId = SmartIdGenerator.nextId();
        entity.setId(newId);
        // 创建租户默认的角色
        long roleId = SmartIdGenerator.nextId();
        SysRolePO role = SysRolePO.builder()
                .roleId(roleId)
                .roleName(DEFAULT_ROLE_NAME)
                .roleCode(DEFAULT_ROLE_CODE)
                .superAdminYn(Boolean.TRUE)
                .seq(10)
                .tenantId(newId)
                .build();
        this.sysRoleService.save(role);
        // 创建角色人员关联关系
        SysUserRolePO sysUserRole = new SysUserRolePO();
        sysUserRole.setUserId(ADMIN_USER_ID);
        sysUserRole.setRoleId(roleId);
        sysUserRole.setTenantId(newId);
        this.sysUserRoleService.save(sysUserRole);
        // 绑定管理员用户
        SysTenantUserPO tenantUser = new SysTenantUserPO();
        tenantUser.setTenantId(newId);
        tenantUser.setUserId(ADMIN_USER_ID);
        this.sysTenantUserService.save(tenantUser);
        // 为管理员创建账户
        this.sysUserAccountService.createAccount(newId, List.of(ADMIN_USER_ID));

        return this.save(entity);
    }

    /**
     * 根据用户查询租户
     *
     * @param userId 用户ID
     * @return 租户列表
     */
    @NonNull
    @Override
    public List<SysTenantPO> listTenantByUserId(@NonNull Long userId) {
        ZonedDateTime now = ZonedDateTime.now();
        LambdaQueryWrapper<SysTenantPO> queryWrapper = new LambdaQueryWrapper<>(SysTenantPO.class)
                .eq(SysTenantPO::getUseYn, Boolean.TRUE)
                .and(
                        wrapper -> wrapper.and(query -> query.isNull(SysTenantPO::getEffectTime).isNull(SysTenantPO::getExpireTime))
                                .or(query -> query.le(SysTenantPO::getEffectTime, now).ge(SysTenantPO::getExpireTime, now))
                ).apply("id in (select M.tenant_id from sys_tenant_user M where M.user_id = {0})", userId);
        return this.list(queryWrapper);
    }

    /**
     * 注入租户信息
     *
     * @param dataList 数据列表
     */
    @Override
    public void injectTenant(List<? extends SysTenantInject> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        Set<Long> tenantIds = dataList.stream()
                .map(SysTenantInject::getTenantId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(tenantIds)) {
            return;
        }
        Map<Long, SysTenantPO> tenantMap = this.lambdaQuery()
                .select(
                        SysTenantPO::getId,
                        SysTenantPO::getTenantCode,
                        SysTenantPO::getTenantName,
                        SysTenantPO::getTenantShortName,
                        SysTenantPO::getPlatformYn,
                        SysTenantPO::getUseYn
                ).in(SysTenantPO::getId, tenantIds)
                .list().stream()
                .collect(Collectors.toMap(SysTenantPO::getId, item -> item));
        if (CollectionUtils.isEmpty(tenantMap)) {
            return;
        }
        dataList.forEach(item -> item.setTenant(tenantMap.get(item.getTenantId())));
    }

    /**
     * 指定租户保存用户
     *
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTenantUser(SysTenantSaveUpdateUserDTO parameter) {
        boolean createAccount = Boolean.TRUE.equals(parameter.getCreateAccount());
        if (!createAccount) {
            return this.sysUserService.saveUpdateWithDept(parameter.getTenantId(), parameter);
        }
        return this.sysUserService.saveAndCreateAccount(parameter.getTenantId(), parameter);
    }
}