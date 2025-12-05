package com.smart.module.system.service.monitor.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.system.mapper.monitor.SmartMonitorSlowSqlMapper;
import com.smart.module.system.model.monitor.SmartMonitorSlowSqlPO;
import com.smart.module.system.service.monitor.SmartMonitorSlowSqlService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* smart_monitor_slow_sql - 慢sql记录 Service实现类
* @author SmartCodeGenerator
* 2025年3月1日 20:58:16
*/
@Service
public class SmartMonitorSlowSqlServiceImpl extends BaseServiceImpl<SmartMonitorSlowSqlMapper, SmartMonitorSlowSqlPO> implements SmartMonitorSlowSqlService {

    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<SmartMonitorSlowSqlPO> list(@NonNull QueryWrapper<SmartMonitorSlowSqlPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        // 排除字段
        List<String> excludeProperty = List.of(
                CrudUtils.getJavaProperty(SmartMonitorSlowSqlPO::getParameter),
                CrudUtils.getJavaProperty(SmartMonitorSlowSqlPO::getColumnList)
        );
        queryWrapper.select(SmartMonitorSlowSqlPO.class, column -> !excludeProperty.contains(column.getProperty()));
        return super.list(queryWrapper, parameter, paging);
    }
}