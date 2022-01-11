package com.smart.system.service.impl;

import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.SysAuthUserMapper;
import com.smart.system.model.SysAuthUserPO;
import com.smart.system.service.SysAuthUserService;
import org.springframework.stereotype.Service;

/**
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0.7
 */
@Service
public class SysAuthUserServiceImpl extends BaseServiceImpl<SysAuthUserMapper, SysAuthUserPO> implements SysAuthUserService {
}
