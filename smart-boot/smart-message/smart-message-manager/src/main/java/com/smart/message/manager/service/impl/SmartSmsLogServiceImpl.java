package com.smart.message.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.message.manager.mapper.SmartSmsLogMapper;
import com.smart.message.manager.model.SmartSmsChannelManagerPO;
import com.smart.message.manager.model.SmartSmsLogPO;
import com.smart.message.manager.pojo.vo.SmartSmsLogListVO;
import com.smart.message.manager.service.SmartSmsChannelManagerService;
import com.smart.message.manager.service.SmartSmsLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* smart_sms_log - 短息发送日志 Service实现类
* @author SmartCodeGenerator
* 2023年5月30日
*/
@Service
public class SmartSmsLogServiceImpl extends BaseServiceImpl<SmartSmsLogMapper, SmartSmsLogPO> implements SmartSmsLogService {

    private final SmartSmsChannelManagerService channelManagerService;

    public SmartSmsLogServiceImpl(SmartSmsChannelManagerService channelManagerService) {
        this.channelManagerService = channelManagerService;
    }

    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<? extends SmartSmsLogPO> list(@NonNull QueryWrapper<SmartSmsLogPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SmartSmsLogPO> list = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        Map<Long, SmartSmsChannelManagerPO> channelManagerMap = new HashMap<>(0);
        // 获取通道信息
        Set<Long> channelIds = list.stream()
                .map(SmartSmsLogPO::getChannelId)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(channelIds)) {
            List<SmartSmsChannelManagerPO> channelManagerList = this.channelManagerService.listByIds(channelIds);
            if (!CollectionUtils.isEmpty(channelManagerList)) {
                channelManagerMap = channelManagerList.stream()
                        .collect(Collectors.toMap(SmartSmsChannelManagerPO::getId, item -> item));
            }
        }

        List<SmartSmsLogListVO> voList = new ArrayList<>(list.size());
        for (SmartSmsLogPO item : list) {
            SmartSmsLogListVO vo = new SmartSmsLogListVO();
            BeanUtils.copyProperties(item, vo);
            vo.setChannel(channelManagerMap.get(item.getChannelId()));
            voList.add(vo);
        }
        return voList;
    }
}