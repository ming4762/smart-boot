package com.smart.system.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.utils.Base64Utils;
import com.smart.commons.core.utils.IdGenerator;
import com.smart.commons.core.utils.auth.ShaUtils;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.auth.SysAuthAccessSecretMapper;
import com.smart.system.model.auth.SysAuthAccessSecretPO;
import com.smart.system.pojo.SysAuthAccessSecretListVO;
import com.smart.system.service.auth.SysAuthAccessSecretService;
import com.smart.system.service.tenant.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
* sys_auth_access_secret -  Service实现类
* @author SmartCodeGenerator
* 2023年10月26日 下午7:25:26
*/
@Service
@RequiredArgsConstructor
public class SysAuthAccessSecretServiceImpl extends BaseServiceImpl<SysAuthAccessSecretMapper, SysAuthAccessSecretPO> implements SysAuthAccessSecretService {


    private final SysTenantService sysTenantService;

    /**
     * 插入一条记录（选择字段，策略插入）
     * 重写保存函数，自动生成ACCESS_KEY和 SECRET_KEY
     * @param entity 实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysAuthAccessSecretPO entity) {
        entity.setAccessKey(Base64Utils.encode(ShaUtils.hmacSha1Encrypt(UUID.randomUUID().toString(), IdGenerator.nextId() + "")));
        entity.setSecretKey(Base64Utils.encode(ShaUtils.hmacSha1Encrypt(UUID.randomUUID().toString(), IdGenerator.nextId() + "")));
        return super.save(entity);
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
    public List<? extends SysAuthAccessSecretPO> list(@NonNull QueryWrapper<SysAuthAccessSecretPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SysAuthAccessSecretPO> list = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<SysAuthAccessSecretListVO> voList = list.stream()
                .map(item -> {
                    SysAuthAccessSecretListVO vo = new SysAuthAccessSecretListVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).toList();
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.WITH_ALL))) {
            this.sysTenantService.injectTenant(voList);
        }
        return voList;
    }
}