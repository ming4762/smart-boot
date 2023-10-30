package com.smart.system.service.auth.impl;

import com.smart.commons.core.utils.Base64Utils;
import com.smart.commons.core.utils.IdGenerator;
import com.smart.commons.core.utils.auth.ShaUtils;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.auth.SysAuthAccessSecretMapper;
import com.smart.system.model.auth.SysAuthAccessSecretPO;
import com.smart.system.service.auth.SysAuthAccessSecretService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
* sys_auth_access_secret -  Service实现类
* @author SmartCodeGenerator
* 2023年10月26日 下午7:25:26
*/
@Service
public class SysAuthAccessSecretServiceImpl extends BaseServiceImpl<SysAuthAccessSecretMapper, SysAuthAccessSecretPO> implements SysAuthAccessSecretService {


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
}