package com.smart.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.commons.core.dto.auth.UserAccountData;
import com.smart.framework.crud.service.BaseService;
import com.smart.module.api.system.dto.QueryUserAccountDTO;
import com.smart.module.system.constants.FunctionTypeEnum;
import com.smart.module.system.model.SysFunctionPO;
import com.smart.module.system.model.SysRolePO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.pojo.dbo.SysUserWthAccountBO;
import com.smart.module.system.pojo.dto.user.ListUserByRoleTenantDTO;
import com.smart.module.system.pojo.dto.user.UserSaveUpdateWithDeptDTO;
import com.smart.module.system.pojo.dto.user.UserSetRoleDTO;
import com.smart.module.system.pojo.vo.SysFunctionListVO;
import com.smart.module.system.pojo.vo.user.SysUserListVO;
import com.smart.module.system.pojo.vo.user.SysUserWithDeptDTO;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author jackson
 * 2020/1/24 3:49 下午
 */
public interface SysUserService extends BaseService<SysUserPO> {

    /**
     * 查询用户的角色
     * @param userId 用户Id
     * @return 角色列表
     */
    @NonNull
    List<SysRolePO> listRole(@NonNull Long userId);

    /**
     * 设置人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    <T> void setWithUser(@NonNull List<T> resource);

    /**
     * 设置创建人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    <T> void setWithCreateUser(@NonNull List<T> resource);


    /**
     * 设置更新人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    <T> void setWithUpdateUser(@NonNull List<T> resource);

    /**
     * 查询用户菜单信息
     * @param localeList 语言列表
     * @return 菜单列表
     */
    @NonNull
    List<SysFunctionListVO> listCurrentUserMenu(List<Locale> localeList);

    /**
     * 查询用户功能
     * @param types 查询的功能类型
     * @return 用户ID表
     */
    List<SysFunctionPO> listUserFunction(List<FunctionTypeEnum> types);

    /**
     * 查询用户功能，带有国际化信息
     * @param types 菜单类型
     * @param localeList 语言
     * @return 菜单信息
     */
    List<SysFunctionListVO> listUserFunctionWithLocale(List<FunctionTypeEnum> types, List<Locale> localeList);

    /**
     * 设置角色
     * @param parameter 参数
     * @return 是否这是成功
     */
    boolean setRole(@NonNull UserSetRoleDTO parameter);

    /**
     * 通过角色ID查询用户信息
     * @param roleIdList 角色ID列表
     * @return 用户信息
     */
    List<SysUserPO> listUserByRoleId(List<Long> roleIdList);


    /**
     * 查询用户列表带账号信息
     * @param parameter 参数
     * @return 用户列表
     */
    List<SysUserWthAccountBO> listUserWithAccount(QueryWrapper<SysUserPO> parameter);

    /**
     * 查询用户角色权限信息
     * @param parameter 参数
     * @return 用户账户信息
     */
    UserAccountData queryUserAccount(QueryUserAccountDTO parameter);

    /**
     * 添加/更新用户(带有部门)
     * @param tenantId 租户ID
     * @param parameter 参数
     * @return 是否保存成功
     */
    boolean saveUpdateWithDept(Long tenantId, UserSaveUpdateWithDeptDTO parameter);

    /**
     * 通过手机号查询用户
     * @param mobile 手机号
     * @return 用户信息
     */
    SysUserPO getByMobile(@NonNull String mobile);

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    SysUserPO getByUsername(@NonNull String username);

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @return 重置后的密码
     */
    String resetPassword(Long userId);

    /**
     * 批量查询用户的角色信息
     * @param userIdList 用户ID列表
     * @return 用户角色细腻系
     */
    Map<Long, List<SysRolePO>> listUserRole(List<Long> userIdList);

    /**
     * 通过ID获取用户详情
     * @param userId 用户ID
     * @return 用户详情
     */
    SysUserListVO getDetailById(Long userId);

    /**
     * 通过ID获取用户详情，包含部门ID
     * @param userId 用户ID
     * @return 用户详情
     */
    SysUserWithDeptDTO getUserByIdWithDept(Long userId);

    /**
     * 通过角色ID&租户ID查询用户信息
     * @param parameter 参数
     * @return 用户信息
     */
    List<SysUserPO> listUserByRoleTenant(@Valid ListUserByRoleTenantDTO parameter);

    /**
     * 保存用户信息，同时创建账号信息
     * @param tenantId 租户ID
     * @param parameter 参数
     * @return 是否保存成功
     */
    boolean saveAndCreateAccount(Long tenantId, UserSaveUpdateWithDeptDTO parameter);
}
