package com.smart.db.generator.engine.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.InputStream;

/**
 * 模板对象
 * @author shizhongming
 * 2020/7/3 9:18 上午
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateElement {
    private static final long serialVersionUID = 5203101733171609383L;

    @NonNull
    private String templateName;

    @Nullable
    private InputStream inputStream;
}
