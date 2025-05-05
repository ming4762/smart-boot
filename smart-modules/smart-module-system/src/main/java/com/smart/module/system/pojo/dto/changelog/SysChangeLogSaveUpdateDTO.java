package com.smart.module.system.pojo.dto.changelog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
* sys_change_log - 系统变更日志
* @author SmartCodeGenerator
* 2025年5月5日 19:15:45
*/
@Getter
@Setter
@ToString
public class SysChangeLogSaveUpdateDTO implements Serializable {

    /**
    * id
    */
    private Long id;
    /**
    * 版本号
    */
    private String version;
    /**
    * 变更模块
    */
    private String changeModule;
    /**
    * 变更时间
    */
    private ZonedDateTime logTime;
    /**
    * 标题
    */
    private String title;
    /**
    * 变更内容
    */
    private String changeContent;
    /**
    * 变更类型
    */
    private String changeType;
    /**
    * 影响范围
    */
    private String affectedScope;
    /**
    * 是否发送系统消息
    */
    private Boolean sendSystemMessageYn;
    /**
    * 系统消息ID
    */
    private Long systemMessageId;
    /**
    * 备注
    */
    private String remark;

}