package com.smart.message.manager.service;

import com.smart.crud.service.BaseService;
import com.smart.message.manager.model.SmartMessageSystemPO;
import com.smart.message.manager.pojo.vo.SmartMessageSystemDetailVO;

import java.util.List;

/**
* smart_message_system - 系统消息表 Service
* @author SmartCodeGenerator
* 2023年7月6日 下午4:53:46
*/
public interface SmartMessageSystemService extends BaseService<SmartMessageSystemPO> {

    /**
     * 批量发布消息
     * @param idList id列表
     * @return 是否发布成功
     */
    boolean publish(List<Long> idList);

    /**
     * 发布消息
     * @param data 消息数据
     * @return 是否发布成功
     */
    boolean publish(SmartMessageSystemPO data);

    /**
     * 通过ID查询系统消息详情
     * @param id 消息ID
     * @return 消息详情
     */
    SmartMessageSystemDetailVO getDetailById(Long id);

    /**
     * 撤销消息
     * @param idList ID列表
     * @return 是否撤销成功
     */
    boolean revoke(List<Long> idList);
}