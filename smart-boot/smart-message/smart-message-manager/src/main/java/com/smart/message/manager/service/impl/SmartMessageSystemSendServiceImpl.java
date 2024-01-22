package com.smart.message.manager.service.impl;

import cn.hutool.core.lang.func.LambdaUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.utils.CrudUtils;
import com.smart.message.manager.mapper.SmartMessageSystemSendMapper;
import com.smart.message.manager.model.SmartMessageSystemSendPO;
import com.smart.message.manager.pojo.dbo.SmartMessageSendMessageDO;
import com.smart.message.manager.pojo.paramteter.SmartMessageMarkAsReadParameter;
import com.smart.message.manager.pojo.paramteter.SmartMessageSendParameter;
import com.smart.message.manager.service.SmartMessageSystemSendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
* smart_message_system_send - 系统消息发送阅读记录 Service实现类
* @author SmartCodeGenerator
* 2023年7月14日 下午6:44:49
*/
@Service
public class SmartMessageSystemSendServiceImpl extends BaseServiceImpl<SmartMessageSystemSendMapper, SmartMessageSystemSendPO> implements SmartMessageSystemSendService {

    /**
     * 查询发送的小心
     *
     * @param parameter 参数
     * @return 消息列表
     */
    @Override
    public List<SmartMessageSendMessageDO> listCurrentSendMessage(SmartMessageSendParameter parameter) {
        QueryWrapper<SmartMessageSystemSendPO> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter.getParameter(), SmartMessageSystemSendPO.class);
        if (StringUtils.hasText(parameter.getTitle())) {
            // 标题查询条件
            queryWrapper.like(LambdaUtil.getFieldName(SmartMessageSendParameter::getTitle), parameter.getTitle());
        }
        if (parameter.getMessageType() != null) {
            queryWrapper.eq(LambdaUtil.getFieldName(SmartMessageSendParameter::getMessageType), parameter.getMessageType().getValue());
        }
        queryWrapper.lambda()
                .eq(SmartMessageSystemSendPO::getUserId, AuthUtils.getCurrentUserId());
        return this.baseMapper.listSendMessage(queryWrapper);
    }

    /**
     * 标记为已读
     *
     * @param parameter 参数
     * @return 是否标记成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(SmartMessageMarkAsReadParameter parameter) {
        if (Boolean.TRUE.equals(parameter.getMarkAll())) {
            this.update(
                    new UpdateWrapper<SmartMessageSystemSendPO>().lambda()
                            .set(SmartMessageSystemSendPO::getReadYn, Boolean.TRUE)
                            .set(SmartMessageSystemSendPO::getReadTime, LocalDateTime.now())
                            .eq(SmartMessageSystemSendPO::getReadYn, Boolean.FALSE)
                            .eq(SmartMessageSystemSendPO::getUserId, AuthUtils.getNonNullCurrentUserId())
            );
            return true;
        }
        if (CollectionUtils.isEmpty(parameter.getMessageIdList())) {
            return false;
        }
        Lists.partition(parameter.getMessageIdList(), 900).forEach(list -> this.update(
                new UpdateWrapper<SmartMessageSystemSendPO>().lambda()
                        .set(SmartMessageSystemSendPO::getReadYn, Boolean.TRUE)
                        .set(SmartMessageSystemSendPO::getReadTime, LocalDateTime.now())
                        .eq(SmartMessageSystemSendPO::getReadYn, Boolean.FALSE)
                        .eq(SmartMessageSystemSendPO::getUserId, AuthUtils.getNonNullCurrentUserId())
                        .in(SmartMessageSystemSendPO::getId, list)
        ));
        return true;
    }
}