package com.smart.message.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.message.manager.constants.MessageSendStatusEnum;
import com.smart.message.manager.constants.ReceiveUserTypeEnum;
import com.smart.message.manager.mapper.SmartMessageSystemMapper;
import com.smart.message.manager.model.SmartMessageSystemPO;
import com.smart.message.manager.model.SmartMessageSystemSendPO;
import com.smart.message.manager.pojo.vo.SmartMessageSystemDetailVO;
import com.smart.message.manager.service.SmartMessageSystemSendService;
import com.smart.message.manager.service.SmartMessageSystemService;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.api.system.parameter.RemoteSysUserListParameter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
* smart_message_system - 系统消息表 Service实现类
* @author SmartCodeGenerator
* 2023年7月6日 下午4:53:46
*/
@Service
public class SmartMessageSystemServiceImpl extends BaseServiceImpl<SmartMessageSystemMapper, SmartMessageSystemPO> implements SmartMessageSystemService {

    private final SysUserApi sysUserApi;

    private final SmartMessageSystemSendService smartMessageSystemSendService;

    public SmartMessageSystemServiceImpl(SysUserApi sysUserApi, SmartMessageSystemSendService smartMessageSystemSendService) {
        this.sysUserApi = sysUserApi;
        this.smartMessageSystemSendService = smartMessageSystemSendService;
    }

    /**
     * 批量发布消息
     *
     * @param idList id列表
     * @return 是否发布成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        List<SmartMessageSystemPO> messageList = this.listByIds(idList);
        if (CollectionUtils.isEmpty(messageList)) {
            return false;
        }
        return messageList.stream()
                .allMatch(this::publish);
    }

    /**
     * 发布消息
     *
     * @param data 消息数据
     * @return 是否发布成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(SmartMessageSystemPO data) {
        LambdaUpdateWrapper<SmartMessageSystemPO> updateWrapper = new UpdateWrapper<SmartMessageSystemPO>().lambda()
                .set(SmartMessageSystemPO::getSendStatus, MessageSendStatusEnum.SEND.getValue())
                .set(SmartMessageSystemPO::getSendUserId, AuthUtils.getNonNullCurrentUserId())
                .set(SmartMessageSystemPO::getSendTime, LocalDateTime.now());
        List<Long> userIds = data.getUserIds();
        if (ReceiveUserTypeEnum.ALL_USER.equals(data.getReceiveUserType())) {
            userIds = this.sysUserApi.listUser(
                            RemoteSysUserListParameter.builder()
                                    .useYn(Boolean.TRUE)
                                    .build()
                    ).stream().map(SysUserDTO::getUserId)
                    .toList();
            updateWrapper.set(SmartMessageSystemPO::getUserIds, userIds);
        }
        // 更新消息表
        this.update(updateWrapper);
        // 插入阅读记录表
        this.smartMessageSystemSendService.saveBatch(
                userIds.stream()
                        .map(item -> {
                            SmartMessageSystemSendPO model = new SmartMessageSystemSendPO();
                            model.setMessageId(data.getId());
                            model.setUserId(item);
                            return model;
                        }).toList()
        );
        return true;
    }

    /**
     * 通过ID查询系统消息详情
     *
     * @param id 消息ID
     * @return 消息详情
     */
    @Override
    public SmartMessageSystemDetailVO getDetailById(Long id) {
        SmartMessageSystemPO message = this.getById(id);
        if (message == null) {
            return null;
        }
        SmartMessageSystemDetailVO vo = new SmartMessageSystemDetailVO();
        BeanUtils.copyProperties(message, vo);
        if (vo.getSendUserId() != null) {
            List<SysUserDTO> userList = this.sysUserApi.listUserById(List.of(vo.getSendUserId()));
            if (!CollectionUtils.isEmpty(userList)) {
                vo.setSender(userList.get(0));
            }
        }
        return vo;
    }

    /**
     * 撤销消息
     *
     * @param idList ID列表
     * @return 是否撤销成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean revoke(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        return this.update(
                new UpdateWrapper<SmartMessageSystemPO>().lambda()
                        .set(SmartMessageSystemPO::getSendStatus, MessageSendStatusEnum.CANCEL)
                        .set(SmartMessageSystemPO::getCancelTime, LocalDateTime.now())
        ) &&
                this.smartMessageSystemSendService.remove(
                        new QueryWrapper<SmartMessageSystemSendPO>().lambda()
                                .in(SmartMessageSystemSendPO::getMessageId, idList)
                );
    }
}