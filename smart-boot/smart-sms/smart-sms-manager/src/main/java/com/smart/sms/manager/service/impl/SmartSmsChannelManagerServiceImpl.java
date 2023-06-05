package com.smart.sms.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.commons.core.exception.BusinessException;
import com.smart.commons.core.exception.SystemException;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.sms.core.parameter.SmsSendParameter;
import com.smart.sms.core.result.SmsSendResult;
import com.smart.sms.core.service.SmartSmsService;
import com.smart.sms.manager.mapper.SmartSmsChannelManagerMapper;
import com.smart.sms.manager.model.SmartSmsChannelManagerPO;
import com.smart.sms.manager.pojo.parameter.SmartSmsSendTestParameter;
import com.smart.sms.manager.service.SmartSmsChannelManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* smart_sms_channel_manager - 短信通道表 Service实现类
* @author SmartCodeGenerator
* 2023年5月26日 上午9:34:41
*/
@Service
@Slf4j
public class SmartSmsChannelManagerServiceImpl extends BaseServiceImpl<SmartSmsChannelManagerMapper, SmartSmsChannelManagerPO> implements SmartSmsChannelManagerService {

    private final SmartSmsService smartSmsService;

    public SmartSmsChannelManagerServiceImpl(@Lazy SmartSmsService smartSmsService) {
        this.smartSmsService = smartSmsService;
    }

    /**
     * 设置默认通道
     *
     * @param id 通道ID
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(Long id) {
        SmartSmsChannelManagerPO manager = this.getById(id);
        if (manager == null) {
            log.warn("id不存在无法设置默认，id：{}", id);
            throw new BusinessException("不存在的ID");
        }
        if (Boolean.TRUE.equals(manager.getIsDefault())) {
            return true;
        }
        // 将现有的默认修改为false
        this.update(
                new UpdateWrapper<SmartSmsChannelManagerPO>().lambda()
                        .set(SmartSmsChannelManagerPO::getIsDefault, Boolean.FALSE)
                        .eq(SmartSmsChannelManagerPO::getIsDefault, Boolean.TRUE)
        );
        return this.update(
                new UpdateWrapper<SmartSmsChannelManagerPO>().lambda()
                        .set(SmartSmsChannelManagerPO::getIsDefault, Boolean.TRUE)
                        .eq(SmartSmsChannelManagerPO::getId, id)
        );
    }

    /**
     * 获取默认的通道
     *
     * @return 默认短信通道信息
     */
    @Override
    public SmartSmsChannelManagerPO getDefault() {
        List<SmartSmsChannelManagerPO> list = this.list(
                new QueryWrapper<SmartSmsChannelManagerPO>().lambda()
                        .eq(SmartSmsChannelManagerPO::getIsDefault, Boolean.TRUE)
                        .eq(SmartSmsChannelManagerPO::getUseYn, Boolean.TRUE)
                        .eq(SmartSmsChannelManagerPO::getDeleteYn, Boolean.FALSE)
        );
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            throw new SystemException("系统发生错误，存在多个默认的短信通道，通道ID：" + list.stream().map(item -> item.getId().toString()).collect(Collectors.joining(",")));
        }
        return list.get(0);
    }

    /**
     * 通过code获取短信通道
     *
     * @param code 通道code
     * @return 短信通道机械能西
     */
    @Override
    public SmartSmsChannelManagerPO getByCode(String code) {
        return this.getOne(
                new QueryWrapper<SmartSmsChannelManagerPO>().lambda()
                        .eq(SmartSmsChannelManagerPO::getChannelCode, code)
                        .eq(SmartSmsChannelManagerPO::getUseYn, Boolean.TRUE)
                        .eq(SmartSmsChannelManagerPO::getDeleteYn, Boolean.FALSE)
        );
    }

    /**
     * 发送短息你测试
     *
     * @param parameter 参数
     * @return 是否发送成功
     */
    @Override
    public SmsSendResult sendTest(SmartSmsSendTestParameter parameter) {
        SmsSendParameter sendParameter = new SmsSendParameter();
        BeanUtils.copyProperties(parameter, sendParameter);
        return this.smartSmsService.send(parameter.getChannelId(), sendParameter);
    }
}