package com.smart.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.smart.crud.mapper.CrudBaseMapper;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.dbo.SysUserWthAccountBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jackson
 * 2020/1/23 7:44 下午
 */
public interface SysUserMapper extends CrudBaseMapper<SysUserPO> {

    /**
     * 查询用户列表带账号信息
     * @param wrapper 参数
     * @return 用户列表
     */
    List<SysUserWthAccountBO> listUserWithAccount(@Param(Constants.WRAPPER) Wrapper<SysUserPO> wrapper);
}
