package com.smart.monitor.server.manager.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* monitor_application - 应用管理表
* @author GCCodeGenerator
* 2022-2-8 16:24:43
*/
@Getter
@Setter
@ToString
public class MonitorApplicationSaveUpdateDTO implements Serializable {


    private static final long serialVersionUID = 3968885198685798549L;

    private Long id;

    /**
    * 应用名称
    */
    @NotNull(message = "应用编码不能为空")
    private String name;
    /**
    * 应用编码
    */
    @NotNull(message = "应用名称不能为空")
    private String applicationCode;
    /**
    * 备注
    */
    private String remark;
    /**
    * 序号
    */
    @NotNull(message = "序号不能为空")
    private Integer seq;
    /**
    * 是否使用
    */
    private Boolean useYn;
    /**
    * 客户端状态检测间隔时间
    */
    private Long statusInterval;
    /**
    * 客户端离线检测事件间隔
    */
    private Long offlineInterval;
    /**
    * 与客户端交互token
    */
    private String token;
    /**
    * 序列化的事件编码列表
    */
    private String serializeEventCode;

}
