package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.smart.commons.core.http.HttpMethod;
import com.smart.crud.model.BaseModelUserTime;
import com.smart.system.constants.FunctionTypeEnum;
import com.smart.system.mybatis.type.FunctionTypeTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author jackson
 * 2020/1/27 12:13 下午
 */
@TableName(value = "sys_function", autoResultMap = true)
@Getter
@Setter
public class SysFunctionPO extends BaseModelUserTime {


    @Serial
    private static final long serialVersionUID = -4732658608405383250L;
    /**
     * 功能ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long functionId;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 功能名称
     */
    private String functionName;

    /**
     * 功能类型（10：目录 20：菜单 30：功能）
     */
    @TableField(typeHandler = FunctionTypeTypeHandler.class)
    private FunctionTypeEnum functionType;

    /**
     * 国际化编码
     */
    private String i18nCode;

    /**
     * 图标
     */
    private String icon;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * url
     */
    private String url;

    /**
     * 权限
     */
    private String permission;

    /**
     * 是否菜单
     */
    private Boolean isMenu;

    /**
     * 外链菜单打开方式 0/内部打开 1/外部打开
     */
    private Boolean internalOrExternal;

    /**
     * 是否配置数据权限
     */
    private Boolean dataRule;

    /**
     * 请求方式
     */
    private HttpMethod httpMethod;

    /**
     * 组件
     */
    private String component;

    /**
     * 组件名称
     */
    private String componentName;

    private String redirect;

    /**
     * 是否缓存路由
     */
    private Boolean cached;

    private Boolean hasChild;

    private Boolean useYn;

    @TableLogic
    private Boolean deleteYn;

}
