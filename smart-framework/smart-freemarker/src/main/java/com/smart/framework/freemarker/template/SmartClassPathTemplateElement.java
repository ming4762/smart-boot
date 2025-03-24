package com.smart.framework.freemarker.template;

import lombok.Getter;

/**
 * 从classpath中获取模板
 * @author shizhongming
 * 2024/11/11 10:13
 * @since 5.0.0
 */
@Getter
public class SmartClassPathTemplateElement extends AbstractSmartTemplateElement {

    public SmartClassPathTemplateElement(String classPath) {
        super(classPath);
    }
}
