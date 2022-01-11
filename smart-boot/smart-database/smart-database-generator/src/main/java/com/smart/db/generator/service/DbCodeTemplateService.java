package com.smart.db.generator.service;

import com.smart.crud.service.BaseService;
import com.smart.db.generator.model.DbCodeTemplatePO;
import com.smart.db.generator.pojo.dto.DbTemplateUserGroupSaveDTO;

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
