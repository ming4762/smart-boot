package com.smart.system.service.tenant.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.smart.commons.core.exception.BusinessException;
import com.smart.crud.plus.metadata.SmartTableInfo;
import com.smart.crud.query.IdParameter;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.utils.CrudUtils;
import com.smart.system.mapper.tenant.SysTenantMapper;
import com.smart.system.mapper.tenant.SysTenantUserMapper;
import com.smart.system.model.SysUserPO;
import com.smart.system.model.tenant.SysTenantPO;
import com.smart.system.model.tenant.SysTenantPackagePO;
import com.smart.system.model.tenant.SysTenantUserPO;
import com.smart.system.pojo.dbo.tenant.SysTenantUserListDO;
import com.smart.system.pojo.dto.tenant.SysTenantBindUserDTO;
import com.smart.system.pojo.dto.tenant.SysTenantListNoBindUserDTO;
import com.smart.system.pojo.dto.tenant.SysTenantRemoveBindUserDTO;
import com.smart.system.pojo.dto.tenant.SysTenantUserListDTO;
import com.smart.system.service.SysUserService;
import com.smart.system.service.tenant.SysTenantPackageService;
import com.smart.system.service.tenant.SysTenantService;
import com.smart.system.service.tenant.SysTenantUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
* sys_tenant - 租户表 Service实现类
* @author SmartCodeGenerator
* 2024年3月29日 上午10:42:40
*/
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl extends BaseServiceImpl<SysTenantMapper, SysTenantPO> implements SysTenantService {

    private final SysTenantUserMapper sysTenantUserMapper;
    private final SysUserService sysUserService;
    private final SysTenantUserService sysTenantUserService;
    private final SysTenantPackageService sysTenantPackageService;

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
        List<SysTenantUserPO> modelList = parameter.getUserIdList().stream()
                .map(userId -> {
                    SysTenantUserPO tenantUser = new SysTenantUserPO();
                    tenantUser.setTenantId(parameter.getTenantId());
                    tenantUser.setUserId(userId);
                    return tenantUser;
                }).toList();
        return this.sysTenantUserService.saveBatch(modelList);
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
        return this.sysTenantUserService.remove(
                new LambdaQueryWrapper<>(SysTenantUserPO.class)
                        .eq(SysTenantUserPO::getTenantId, parameter.getTenantId())
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
            throw new BusinessException("不能删除不可编辑租户");
        }
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
                throw new BusinessException("不能修改不可编辑租户");
            }
        }
        return super.saveOrUpdateBatch(entityList);
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
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<SysTenantPO> queryWrapper = new LambdaQueryWrapper<>(SysTenantPO.class)
                .eq(SysTenantPO::getUseYn, Boolean.TRUE)
                .and(
                        wrapper -> wrapper.and(query -> query.isNull(SysTenantPO::getEffectTime).isNull(SysTenantPO::getExpireTime))
                                .or(query -> query.le(SysTenantPO::getExpireTime, now).ge(SysTenantPO::getExpireTime, now))
                ).apply("id in (select M.tenant_id from sys_tenant_user M where M.user_id = {0})", userId);
        return this.list(queryWrapper);
    }
}