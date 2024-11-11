package com.smart.framework.freemarker.template;

import lombok.Getter;

/**
 * 模板引擎元素
 * @author shizhongming
 * 2024/11/11 10:11
 * @since 5.0.0
 */
@Getter
public abstract class SmartTemplateElement {

    private final String name;

    public SmartTemplateElement(String name) {
        this.name = name;
    }
}
