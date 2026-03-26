package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.crud.constants.CrudCommonEnum;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.system.mapper.SysSystemMapper;
import com.smart.module.system.model.SysSystemPO;
import com.smart.module.system.model.SysSystemUserPO;
import com.smart.module.system.pojo.dto.system.SystemSetUserDTO;
import com.smart.module.system.service.SysSystemService;
import com.smart.module.system.service.SysSystemUserService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<SysSystemPO> list(@NonNull QueryWrapper<SysSystemPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        // 判断是否需要根据人员过滤
        boolean isFilterUser = Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.FILTER_BY_USER.name()));
        if (isFilterUser && !AuthUtils.isSuperAdmin()) {
            String tableName = CrudUtils.getTableName(SysSystemUserPO.class);
            queryWrapper.apply(String.format("id in (select system_id from %s where user_id = {0})", tableName), AuthUtils.getCurrentUserId());
        }
        return super.list(queryWrapper, parameter, paging);
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
                            }).toList()
            );
        }
        return true;
    }
}
