package com.smart.system.service.auth;

import com.smart.crud.service.BaseService;
import com.smart.system.model.auth.SmartAuthLicensePO;

import java.io.OutputStream;
import java.io.Serializable;

/**
* smart_auth_license - 许可证管理 Service
* @author SmartodeGenerator
* 2022-12-17 14:31:50
*/
public interface SmartAuthLicenseService extends BaseService<SmartAuthLicensePO> {

    /**
     * 生成license
     * @param id id
     * @return 是否生成成功
     */
    boolean generator(Long id);

    /**
     * 下载license
     * @param id license id
     * @param outputStream 输出流
     */
    void download(Serializable id, OutputStream outputStream);
}
