package com.smart.module.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.api.system.parameter.RemoteSysUserListParameter;
import com.smart.module.message.constants.MessageSendStatusEnum;
import com.smart.module.message.constants.ReceiveUserTypeEnum;
import com.smart.module.message.mapper.SmartMessageSystemMapper;
import com.smart.module.message.model.SmartMessageSystemPO;
import com.smart.module.message.model.SmartMessageSystemSendPO;
import com.smart.module.message.pojo.vo.SmartMessageSystemDetailVO;
import com.smart.module.message.service.SmartMessageSystemSendService;
import com.smart.module.message.service.SmartMessageSystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;

/**
* smart_message_system - 系统消息表 Service实现类
* @author SmartCodeGenerator
* 2023年7月6日 下午4:53:46
*/
@Service
public class SmartMessageSystemServiceImpl extends BaseServiceImpl<SmartMessageSystemMapper, SmartMessageSystemPO> implements SmartMessageSystemService {

    private static final String SYSTEM_MESSAGE_CHANNEL = "SYSTEM_MESSAGE";
    private static final String WEB_SOCKET_MESSAGE_CHANNEL = "WEB_SOCKET";

    private final SysUserApi sysUserApi;

    private final SmartMessageSystemSendService smartMessageSystemSendService;

    private final SmartMessageApi smartMessageApi;

    public SmartMessageSystemServiceImpl(SysUserApi sysUserApi, SmartMessageSystemSendService smartMessageSystemSendService, @Lazy SmartMessageApi smartMessageApi) {
        this.sysUserApi = sysUserApi;
        this.smartMessageSystemSendService = smartMessageSystemSendService;
        this.smartMessageApi = smartMessageApi;
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

        List<Long> userIds = data.getUserIds();
        if (ReceiveUserTypeEnum.ALL_USER.equals(data.getReceiveUserType())) {
            userIds = this.sysUserApi.listUser(
                            RemoteSysUserListParameter.builder()
                                    .useYn(Boolean.TRUE)
                                    .build()
                    ).stream().map(SysUserDTO::getUserId)
                    .toList();
        }
        // 发送消息
        this.smartMessageApi.send(
                RemoteMessageSendParameter.builder()
                        .messageId(data.getId())
                        .toUserIds(new HashSet<>(userIds))
                        .content(data.getContent())
                        .priority(data.getPriority())
                        .messageChannelCodeList(List.of(SYSTEM_MESSAGE_CHANNEL, WEB_SOCKET_MESSAGE_CHANNEL))
                        .build()
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
                        .set(SmartMessageSystemPO::getCancelTime, ZonedDateTime.now())
        ) &&
                this.smartMessageSystemSendService.remove(
                        new QueryWrapper<SmartMessageSystemSendPO>().lambda()
                                .in(SmartMessageSystemSendPO::getMessageId, idList)
                );
    }
}