package com.smart.monitor.server.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2021/3/13 8:45 下午
 */
@Getter
@Setter
@TableName(value = "monitor_application", autoResultMap = true)
public class MonitorApplicationPO extends BaseModelUserTime {
    @Serial
    private static final long serialVersionUID = 8851568305606154497L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private String applicationCode;

    private String remark;

    private Boolean useYn;

    private Boolean deleteYn;

    private Integer seq;

    /**
     * 客户端状态检测间隔事件
     */
    private Long statusInterval;

    /**
     * 客户端离线检测事件间隔
     */
    private Long offlineInterval;

    private String token;

    /**
     * 序列化的事件编码
     */
    private String serializeEventCode;

    /**
     * 通知的事件编码
     */
    private String notifyEventCode;

    /**
     * 通知邮箱
     */
    private String notifyMails;

}
