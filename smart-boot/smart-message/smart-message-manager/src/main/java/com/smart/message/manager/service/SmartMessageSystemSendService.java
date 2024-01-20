package com.smart.message.manager.service;

import com.smart.crud.service.BaseService;
import com.smart.message.manager.model.SmartMessageSystemSendPO;
import com.smart.message.manager.pojo.dbo.SmartMessageSendMessageDO;
import com.smart.message.manager.pojo.paramteter.SmartMessageMarkAsReadParameter;
import com.smart.message.manager.pojo.paramteter.SmartMessageSendParameter;

import java.util.List;

/**
* smart_message_system_send - 系统消息发送阅读记录 Service
* @author SmartCodeGenerator
* 2023年7月14日 下午6:44:49
*/
public interface SmartMessageSystemSendService extends BaseService<SmartMessageSystemSendPO> {

    /**
     * 查询发送的小心
     * @param parameter 参数
     * @return 消息列表
     */
    List<SmartMessageSendMessageDO> listCurrentSendMessage(SmartMessageSendParameter parameter);

    /**
     * 标记为已读
     * @param parameter 参数
     * @return 是否标记成功
     */
    boolean markAsRead(SmartMessageMarkAsReadParameter parameter);
}