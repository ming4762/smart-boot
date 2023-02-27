package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.service.BaseServiceImpl;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.smart.system.model.SysParameterPO;
import com.smart.system.service.SysParameterService;
import com.smart.system.mapper.SysParameterMapper;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
* sys_parameter - 系统参数表 Service实现类
* @author SmartCodeGenerator
* 2023-2-27
*/
@Service
public class SysParameterServiceImpl extends BaseServiceImpl<SysParameterMapper, SysParameterPO> implements SysParameterService {

    /**
     * 获取参数值
     * @param code 系统参数编码
     * @return 系统参数值
     */
    @Override
    @Nullable
    public String getParameter(@NonNull String code) {
        List<SysParameterPO> list = this.list(
                new QueryWrapper<SysParameterPO>().lambda()
                        .select(SysParameterPO::getParameter)
                        .eq(SysParameterPO::getCode, code)
                        .eq(SysParameterPO::getUseYn, true)
        );
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0).getParameter();
    }
}