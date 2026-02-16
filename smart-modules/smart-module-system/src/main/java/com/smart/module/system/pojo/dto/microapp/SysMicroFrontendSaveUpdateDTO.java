package com.smart.module.system.pojo.dto.microapp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* sys_micro_frontend - 前端微
* @author SmartCodeGenerator
* 2026年1月20日 15:31:23
*/
@Getter
@Setter
@ToString
public class SysMicroFrontendSaveUpdateDTO implements Serializable {

    /**
    * id
    */
    private Long id;
    /**
    * 应用编码
    */
    private String code;
    /**
    * 应用名称
    */
    private String name;
    /**
    * 应用地址
    */
    private String url;
    /**
    * 应用HTML
    */
    private String html;
    /**
    * 是否同步
    */
    private Boolean sync;
    /**
    * 短路径的能力
    */
    private String prefix;
    /**
    * 是否存活
    */
    private Boolean alive;
    /**
    * 注入给子应用的数据
    */
    private String props;
    /**
    * 是否启用Fiber
    */
    private Boolean fiber;
    /**
    * 是否降级
    */
    private Boolean degrade;
    /**
    * 自定义iframe属性，json格式
    */
    private String attrs;
    /**
    * 备注
    */
    private String remark;
    /**
    * 排序
    */
    private Integer seq;

}