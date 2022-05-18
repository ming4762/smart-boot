package com.smart.module.document.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.module.document.mapper.DocumentTemplateMapper;
import com.smart.module.document.model.DocumentTemplatePO;
import com.smart.module.document.service.DocumentTemplateService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shizhongming
 * 2021/8/14 8:13 下午
 */
@Service
public class DocumentTemplateServiceImpl extends BaseServiceImpl<DocumentTemplateMapper, DocumentTemplatePO> implements DocumentTemplateService {

    private static final List<String> EXCLUDE_FIELDS = Lists.newArrayList("data");

    @Override
    public List<? extends DocumentTemplatePO> list(@NonNull QueryWrapper<DocumentTemplatePO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.lambda()
                .select(DocumentTemplatePO.class, field -> !EXCLUDE_FIELDS.contains(field.getProperty()));
        return super.list(queryWrapper, parameter, paging);
    }




    @Override
    public DocumentTemplatePO getByCode(@NonNull String templateCode) {
        return this.getOne(new QueryWrapper<DocumentTemplatePO>().lambda().eq(DocumentTemplatePO :: getTemplateCode, templateCode));
    }
}
