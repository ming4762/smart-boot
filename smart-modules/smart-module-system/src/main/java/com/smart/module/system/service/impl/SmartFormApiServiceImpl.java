package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.crud.model.BaseModel;
import com.smart.framework.crud.model.Sort;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.system.controller.api.form.dto.SmartFormTableSelectApiDTO;
import com.smart.module.system.controller.api.form.vo.SmartFormTableSelectApiVO;
import com.smart.module.system.mapper.SmartFormApiMapper;
import com.smart.module.system.service.SmartFormApiService;
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
        SmartTableInfo tableInfo = CrudUtils.getTableInfo(clazz);
        String tableName = tableInfo.getTableName();
        // 获取字典名
        String labelName = tableInfo.getTableFiled(parameter.getLabelFieldName()).getColumn();
        String valueName = null;
        TableFieldInfo tableFiled = tableInfo.getTableFiled(parameter.getValueFieldName());
        if (tableFiled != null) {
            valueName = tableFiled.getColumn();
        } else if (StringUtils.equals(tableInfo.getKeyProperty(), parameter.getValueFieldName())) {
            valueName = tableInfo.getKeyColumn();
        }
        if (valueName == null) {
            throw new SystemException("获取表value字段失败");
        }
        // 创建查询条件
        QueryWrapper<? extends BaseModel> queryWrapper = null;
        PageSortQuery pageSortQuery = parameter.getQueryParameter();
        if (pageSortQuery != null) {
            queryWrapper = CrudUtils.createQueryWrapperFromParameters(
                    pageSortQuery.getParameter() == null ? HashMap.newHashMap(0) : pageSortQuery.getParameter(),
                    clazz
            );
            if (StringUtils.isNotBlank(pageSortQuery.getSortName())) {
                // 处理排序
                List<Sort> sortList = CrudUtils.analysisOrder(pageSortQuery.getSortName(), pageSortQuery.getSortOrder(), clazz);
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
