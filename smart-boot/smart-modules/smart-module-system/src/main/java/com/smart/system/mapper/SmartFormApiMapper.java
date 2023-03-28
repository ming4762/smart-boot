package com.smart.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.smart.crud.model.BaseModel;
import com.smart.system.controller.api.form.vo.SmartFormTableSelectApiVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/2/18
 */
public interface SmartFormApiMapper {

    /**
     * 查询表格列表
     * @param tableName 表名
     * @param valueName value字段名
     * @param labelName label字段名
     * @param queryWrapper 查询条件
     * @return 表格数据列表
     */
    List<SmartFormTableSelectApiVO> listTableSelect(
            @Param("tableName") String tableName,
            @Param("valueName") String valueName,
            @Param("labelName") String labelName,
            @Param(Constants.WRAPPER) QueryWrapper<? extends BaseModel> queryWrapper
    );
}
