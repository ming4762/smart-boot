package com.smart.module.message.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.smart.framework.crud.mapper.CrudBaseMapper;
import com.smart.module.message.model.SmartMessageSystemSendPO;
import com.smart.module.message.pojo.dbo.SmartMessageSendMessageDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* smart_message_system_send - 系统消息发送阅读记录 mapper层
* @author SmartCodeGenerator
* 2023年7月14日 下午6:44:49
*/
public interface SmartMessageSystemSendMapper extends CrudBaseMapper<SmartMessageSystemSendPO> {

    /**
     * 查询发送的消息
     * @param parameter 参数
     * @return 消息记录
     */
    List<SmartMessageSendMessageDO> listSendMessage(@Param(Constants.WRAPPER)Wrapper<SmartMessageSystemSendPO> parameter);

}