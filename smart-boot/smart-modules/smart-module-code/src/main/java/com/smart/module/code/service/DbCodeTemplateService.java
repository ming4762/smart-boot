package com.smart.module.code.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.code.model.DbCodeTemplatePO;
import com.smart.module.code.pojo.dto.DbTemplateUserGroupSaveDTO;

/**
 * @author ShiZhongMing
 * 2021/5/7 17:16
 * @since 1.0
 */
public interface DbCodeTemplateService extends BaseService<DbCodeTemplatePO> {

    /**
     * 保存模板对应的用户组
     * @param parameter 参数
     * @return 结果
     */
    boolean saveTemplateUserGroup(DbTemplateUserGroupSaveDTO parameter);
}
