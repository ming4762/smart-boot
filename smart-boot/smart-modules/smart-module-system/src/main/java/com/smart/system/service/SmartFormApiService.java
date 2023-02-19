package com.smart.system.service;

import com.smart.system.controller.smart_form_api.dto.SmartFormTableSelectApiDTO;
import com.smart.system.controller.smart_form_api.vo.SmartFormTableSelectApiVO;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/2/18
 */
public interface SmartFormApiService {

    List<SmartFormTableSelectApiVO> listTableSelect(SmartFormTableSelectApiDTO parameter);
}
