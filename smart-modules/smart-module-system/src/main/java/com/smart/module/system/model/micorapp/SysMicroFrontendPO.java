package com.smart.module.system.model.micorapp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_micro_frontend - 前端微
* @author SmartCodeGenerator
* 2026年1月20日 15:31:23
*/
@Getter
@Setter
@TableName("sys_micro_frontend")
public class SysMicroFrontendPO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * code - 应用编码
    */
    private String code;

    /**
    * name - 应用名称
    */
    private String name;

    /**
    * url - 应用地址
    */
    private String url;

    /**
    * html - 应用HTML，设置后子应用将直接读取该值，没有设置则子应用通过url请求获取
    */
    private String html;

    /**
    * sync - 路由同步开关
    */
    private Boolean sync;

    /**
    * prefix - 短路径的能力, key为短路径, value为长路径
    */
    private String prefix;

    /**
    * alive - 是否保活
    */
    private Boolean alive;

    /**
    * props - 注入给子应用的数据
    */
    private String props;

    /**
    * fiber - 是否启用Fiber
    */
    private Boolean fiber;

    /**
    * degrade - 是否降级
    */
    private Boolean degrade;

    /**
    * attrs - 自定义iframe属性
    */
    private String attrs;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * seq - 排序
    */
    private Integer seq;

    /**
    * use_yn - 是否启用
    */
    @TableUseYnField
    private Boolean useYn;
}