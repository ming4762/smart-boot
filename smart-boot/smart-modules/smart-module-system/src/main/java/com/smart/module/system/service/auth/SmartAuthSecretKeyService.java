package com.smart.module.system.service.auth;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.auth.SmartAuthSecretKeyPO;
import com.smart.module.system.pojo.dto.auth.SmartAuthSecretKeyUploadUpdateDTO;
import jakarta.servlet.ServletOutputStream;

import java.io.Serializable;

/**
* smart_auth_secret_key - 秘钥管理 Service
* @author SmartCodeGenerator
* 2023-2-19 10:57:38
*/
public interface SmartAuthSecretKeyService extends BaseService<SmartAuthSecretKeyPO> {

    /**
     * 上传秘钥
     * @param parameter 参数
     * @return 结果
     */
    boolean saveUpdate(SmartAuthSecretKeyUploadUpdateDTO parameter);

    /**
     * 下载秘钥
     * @param id ID
     * @param outputStream 输出流
     */
    void download(Serializable id, ServletOutputStream outputStream);
}