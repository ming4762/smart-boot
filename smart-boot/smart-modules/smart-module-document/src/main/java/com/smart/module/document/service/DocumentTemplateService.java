package com.smart.module.document.service;

import com.smart.crud.service.BaseService;
import com.smart.module.document.model.DocumentTemplatePO;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author shizhongming
 * 2021/8/14 8:13 下午
 */
public interface DocumentTemplateService extends BaseService<DocumentTemplatePO> {

    /**
     * 通过模板code获取模板信息
     * @param templateCode 模板code
     * @return 模板信息
     */
    @Nullable
    DocumentTemplatePO getByCode(@NonNull String templateCode);
}
