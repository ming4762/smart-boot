package com.smart.framework.freemarker.template;

import lombok.Getter;

import java.io.InputStream;

/**
 * 从inputStream中获取模板
 * @author shizhongming
 * 2024/11/11 10:13
 * @since 5.0.0
 */
@Getter
public class SmartInputStreamTemplateElement extends AbstractSmartTemplateElement {

    private final InputStream inputStream;

    public SmartInputStreamTemplateElement(String name, InputStream inputStream) {
        super(name);
        this.inputStream = inputStream;
    }
}
