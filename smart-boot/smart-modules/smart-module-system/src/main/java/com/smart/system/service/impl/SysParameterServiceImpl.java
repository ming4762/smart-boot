package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.exception.BusinessException;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.SysParameterMapper;
import com.smart.system.model.SysParameterPO;
import com.smart.system.service.SysParameterService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @NonNull
    @Override
    public Map<String, String> getParameter(@NonNull List<String> codeList) {
        if (CollectionUtils.isEmpty(codeList)) {
            return Collections.emptyMap();
        }
        List<SysParameterPO> list = this.list(
                new QueryWrapper<SysParameterPO>().lambda()
                        .select(SysParameterPO::getParameter, SysParameterPO::getCode)
                        .in(SysParameterPO::getCode, codeList)
                        .eq(SysParameterPO::getUseYn, true)
        );
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream()
                .collect(Collectors.toMap(SysParameterPO::getCode, SysParameterPO::getParameter));
    }

    /**
     * 添加更新
     * @param saveList   添加列表
     * @param updateList 更新列表
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUpdate(List<SysParameterPO> saveList, List<SysParameterPO> updateList) {
        if (!CollectionUtils.isEmpty(saveList)) {
            // 验证编码是否重复
            List<SysParameterPO> hasList = this.list(
                    new QueryWrapper<SysParameterPO>().lambda()
                            .select(SysParameterPO::getId)
                            .in(SysParameterPO::getCode, saveList.stream().map(SysParameterPO::getCode).collect(Collectors.toSet()))
            );
            if (!CollectionUtils.isEmpty(hasList)) {
                throw new BusinessException("参数编码已存在！");
            }
            this.saveBatch(saveList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
        return true;
    }
}