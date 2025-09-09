package com.smart.module.system.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.utils.Base64Utils;
import com.smart.framework.commons.core.utils.SmartIdGenerator;
import com.smart.framework.commons.core.utils.auth.SecretUtils;
import com.smart.framework.commons.core.utils.auth.ShaUtils;
import com.smart.framework.crud.constants.CrudCommonEnum;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.mapper.auth.SysAuthAccessSecretMapper;
import com.smart.module.system.model.auth.SysAuthAccessSecretPO;
import com.smart.module.system.pojo.dto.access.SysAccessCreateSignDTO;
import com.smart.module.system.pojo.vo.SysAuthAccessSecretListVO;
import com.smart.module.system.service.auth.SysAuthAccessSecretService;
import com.smart.module.system.service.tenant.SysTenantService;
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
        entity.setAccessKey(Base64Utils.encode(ShaUtils.hmacSha1Encrypt(UUID.randomUUID().toString(), SmartIdGenerator.nextId() + "")));
        entity.setSecretKey(Base64Utils.encode(ShaUtils.hmacSha1Encrypt(UUID.randomUUID().toString(), SmartIdGenerator.nextId() + "")));
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(SysAuthAccessSecretPO entity) {
        if (this.isAdd(entity)) {
            return this.save(entity);
        }
        return this.updateById(entity);
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

    /**
     * 创建签名
     *
     * @param parameter 签名参数
     * @return 签名
     */
    @Override
    public String createSign(SysAccessCreateSignDTO parameter) {
        SysAuthAccessSecretPO accessSecret = this.getById(parameter.getAccessId());
        if (accessSecret == null) {
            throw new SystemException("查询Access secret失败");
        }
        return SecretUtils.createSign(
                parameter.getHttpMethod().name(),
                parameter.getContentType(),
                parameter.getDate(),
                parameter.getNonce(),
                parameter.getTokenPrefix(),
                accessSecret.getAccessKey(),
                accessSecret.getSecretKey());
    }
}