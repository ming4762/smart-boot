package com.smart.module.system.model.micorapp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_function_micro_frontend - 系统功能微应用关联关系表
* @author SmartCodeGenerator
* 2026年1月23日 18:18:18
*/
@Getter
@Setter
@TableName("sys_function_micro_frontend")
public class SysFunctionMicroFrontendPO extends BaseModelCreateUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * function_id - functionId
    */
    private Long functionId;

    /**
    * micro_frontend_id - microFrontendId
    */
    private Long microFrontendId;

    /**
    * multi_instance_yn - 是否多实例
    */
    private Boolean multiInstanceYn;

    /**
    * route_linkage_yn - 是否联动路由，单实例模式才生效
    */
    private Boolean routeLinkageYn;

    /**
    * preload_yn - 是否预加载
    */
    private  Boolean preloadYn;

    /**
    * micro_frontend_url - 前端微应用地址，如果为null，则根据菜单URL生成
    */
    private String microFrontendPageUrl;

    /**
    * micro_frontend_config - 前端微应用配置
    */
    private String microFrontendConfig;

}