package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.SysSystemMapper;
import com.smart.system.model.SysSystemPO;
import com.smart.system.model.SysSystemUserPO;
import com.smart.system.pojo.dto.system.SystemSetUserDTO;
import com.smart.system.service.SysSystemService;
import com.smart.system.service.SysSystemUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

/**
 * @author zhongming4762
 * 2023/1/21 21:21
 */
@Service
public class SysSystemServiceImpl extends BaseServiceImpl<SysSystemMapper, SysSystemPO> implements SysSystemService {

    private final SysSystemUserService sysSystemUserService;

    public SysSystemServiceImpl(SysSystemUserService sysSystemUserService) {
        this.sysSystemUserService = sysSystemUserService;
    }

    /**
     * 设置关联用户信息
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setUser(SystemSetUserDTO parameter) {
        // 1、删除原有配置
        this.sysSystemUserService.remove(
                new QueryWrapper<SysSystemUserPO>().lambda()
                        .eq(SysSystemUserPO::getSystemId, parameter.getSystemId())
        );
        // 2、保存现有配置
        if (!CollectionUtils.isEmpty(parameter.getUserIdList())) {
            this.sysSystemUserService.saveBatch(
                    parameter.getUserIdList().stream()
                            .map(item -> {
                                SysSystemUserPO sysSystemUser = new SysSystemUserPO();
                                sysSystemUser.setSystemId(parameter.getSystemId());
                                sysSystemUser.setUserId(item);
                                return sysSystemUser;
                            }).collect(Collectors.toList())
            );
        }
        return true;
    }
}
