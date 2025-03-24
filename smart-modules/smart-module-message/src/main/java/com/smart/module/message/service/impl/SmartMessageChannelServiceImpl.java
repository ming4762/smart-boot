package com.smart.module.message.service.impl;

import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.message.mapper.SmartMessageChannelMapper;
import com.smart.module.message.model.SmartMessageChannelPO;
import com.smart.module.message.service.SmartMessageChannelService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
* smart_message_channel - 消息通道信息 Service实现类
* @author SmartCodeGenerator
* 2024年5月17日 下午5:13:58
*/
@Service
public class SmartMessageChannelServiceImpl extends BaseServiceImpl<SmartMessageChannelMapper, SmartMessageChannelPO> implements SmartMessageChannelService {


    /**
     * 重写批量删除方法，如果ID只有一个调用removeById方法
     *
     * @param idList ID列表
     * @return 删除结果
     */
    @Override
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 查询是否有内置数据
        Long buildInCount = this.lambdaQuery()
                .in(SmartMessageChannelPO::getId, idList)
                .eq(SmartMessageChannelPO::getBuiltInYn, Boolean.TRUE)
                .count();
        if (buildInCount > 0) {
            throw new BusinessException("内置通道不能删除");
        }
        return super.removeByIds(idList);
    }
}