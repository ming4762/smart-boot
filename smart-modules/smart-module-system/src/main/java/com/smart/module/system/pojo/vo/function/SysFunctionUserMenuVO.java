package com.smart.module.system.pojo.vo.function;

import com.smart.module.system.model.SysFunctionPO;
import com.smart.module.system.model.micorapp.SysFunctionMicroFrontendPO;
import com.smart.module.system.model.micorapp.SysMicroFrontendPO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * 用户菜单信息VO
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-01-27 13:42
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class SysFunctionUserMenuVO extends SysFunctionPO {


    /**
     * 国际化信息
     * @since 1.0.7
     * 2021-11-12
     */
    private Map<String, String> locales;

    /**
     * 页面的微应用配置
     */
    private SysFunctionMicroFrontendPO functionMicroFrontend;

    /**
     * 微应用信息
     */
    private SysMicroFrontendPO microFrontend;

}
