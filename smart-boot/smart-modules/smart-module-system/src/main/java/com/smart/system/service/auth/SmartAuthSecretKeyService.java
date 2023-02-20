package com.smart.system.service.auth;

import com.smart.crud.service.BaseService;
import com.smart.system.model.auth.SmartAuthSecretKeyPO;
import com.smart.system.pojo.dto.auth.SmartAuthSecretKeyUploadUpdateDTO;

import javax.servlet.ServletOutputStream;
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