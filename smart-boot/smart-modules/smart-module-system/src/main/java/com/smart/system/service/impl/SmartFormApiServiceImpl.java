package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.exception.SystemException;
import com.smart.crud.model.BaseModel;
import com.smart.crud.model.Sort;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.utils.CrudUtils;
import com.smart.system.controller.smart_form_api.dto.SmartFormTableSelectApiDTO;
import com.smart.system.controller.smart_form_api.vo.SmartFormTableSelectApiVO;
import com.smart.system.mapper.SmartFormApiMapper;
import com.smart.system.service.SmartFormApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/2/18
 */
@Service
public class SmartFormApiServiceImpl implements SmartFormApiService {

    private final SmartFormApiMapper smartFormApiMapper;

    public SmartFormApiServiceImpl(SmartFormApiMapper smartFormApiMapper) {
        this.smartFormApiMapper = smartFormApiMapper;
    }

    @Override
    public List<SmartFormTableSelectApiVO> listTableSelect(SmartFormTableSelectApiDTO parameter) {
        // 1、获取表名
        Class<?> clazz;
        try {
            clazz = Class.forName(parameter.getModelClassName());
        } catch (ClassNotFoundException e) {
            throw new SystemException(String.format("找不到实体类，请检查类名是否正确，限定名：%s", parameter.getModelClassName()), e);
        }
        if (!BaseModel.class.isAssignableFrom(clazz)) {
            throw new SystemException(String.format("不是实体类，请检查类名是否正确，限定名：：%s", parameter.getModelClassName()));
        }
        String tableName = CrudUtils.getTableName((Class<? extends BaseModel>) clazz);
        // 获取字典名
        String labelName = CrudUtils.getDbField((Class<? extends BaseModel>) clazz, parameter.getLabelFieldName());
        String valueName = CrudUtils.getDbField((Class<? extends BaseModel>) clazz, parameter.getValueFieldName());
        // 创建查询条件
        QueryWrapper<? extends BaseModel> queryWrapper = null;
        PageSortQuery pageSortQuery = parameter.getQueryParameter();
        if (pageSortQuery != null) {
            queryWrapper = CrudUtils.createQueryWrapperFromParameters(
                    pageSortQuery.getParameter() == null ? new HashMap<>() : pageSortQuery.getParameter(),
                    clazz
            );
            if (StringUtils.isNotBlank(pageSortQuery.getSortName())) {
                // 处理排序
                List<Sort> sortList = CrudUtils.analysisOrder(pageSortQuery.getSortName(), pageSortQuery.getSortOrder(), (Class<? extends BaseModel>) clazz);
                for (Sort sort : sortList) {
                    queryWrapper.orderBy(true, StringUtils.endsWithIgnoreCase(sort.getOrder(), "asc"), sort.getDbName());
                }
            }
        }
        if (queryWrapper == null) {
            queryWrapper = new QueryWrapper<>();
        }
        return this.smartFormApiMapper.listTableSelect(tableName, valueName, labelName, queryWrapper);
    }
}
