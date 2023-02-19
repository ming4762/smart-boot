package com.smart.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.model.BaseModel;
import com.smart.system.controller.smart_form_api.vo.SmartFormTableSelectApiVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/2/18
 */
public interface SmartFormApiMapper {

    List<SmartFormTableSelectApiVO> listTableSelect(
            @Param("tableName") String tableName,
            @Param("valueName") String valueName,
            @Param("labelName") String labelName,
            @Param("queryWrapper") QueryWrapper<? extends BaseModel> queryWrapper
    );
}
