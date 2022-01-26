package com.smart.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.crud.service.BaseService;
import com.smart.system.constants.FunctionTypeEnum;
import com.smart.system.model.SysFunctionPO;
import com.smart.system.model.SysRolePO;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.dbo.SysUserWthAccountBO;
import com.smart.system.pojo.dto.user.UserSetRoleDTO;
import com.smart.system.pojo.vo.SysFunctionListVO;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Locale;

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
     * @param userId 用户ID
     * @param types 查询的功能类型
     * @return 用户ID表
     */
    List<SysFunctionPO> listUserFunction(@NonNull Long userId, List<FunctionTypeEnum> types);

    /**
     * 查询用户功能，带有国际化信息
     * @param userId 用户ID
     * @param types 菜单类型
     * @param localeList 语言
     * @return 菜单信息
     */
    List<SysFunctionListVO> listUserFunctionWithLocale(@NonNull Long userId, List<FunctionTypeEnum> types, List<Locale> localeList);

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
     * 更改密码
     * @param password 密码
     * @param userId 用户ID
     * @return 是否修改成功
     */
    boolean changePassword(@NonNull Long userId, @NonNull String password);


    /**
     * 查询用户列表带账号信息
     * @param parameter 参数
     * @return 用户列表
     */
    List<SysUserWthAccountBO> listUserWithAccount(LambdaQueryWrapper<SysUserPO> parameter);
}
