package com.smart.module.system.pojo.parameter.function;

import com.smart.framework.commons.core.http.HttpMethod;
import com.smart.module.system.constants.FunctionTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 功能保存更新参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-01-26 16:59
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SysFunctionSaveUpdateParameter implements Serializable {

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
     * 是否微前端微应用
     */
    private Boolean isMicroFrontend;

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

    /**
     * 菜单meta
     */
    private String meta;


    private MicroFrontendParameter microFrontend;

    @Getter
    @Setter
    @ToString
    public static class MicroFrontendParameter implements Serializable {
        /**
         * 微前端微应用ID
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
}
