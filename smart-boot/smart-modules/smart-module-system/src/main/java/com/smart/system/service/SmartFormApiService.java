package com.smart.system.service;

import com.smart.system.controller.api.form.dto.SmartFormTableSelectApiDTO;
import com.smart.system.controller.api.form.vo.SmartFormTableSelectApiVO;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/2/18
 */
public interface SmartFormApiService {

    /**
     * 查询表格数据列表
     * @param parameter 参数
     * @return 表格数据列表
     */
    List<SmartFormTableSelectApiVO> listTableSelect(SmartFormTableSelectApiDTO parameter);
}
