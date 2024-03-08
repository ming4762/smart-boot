package com.smart.commons.core.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/3/7 15:24
 * @since 3.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmartFont implements Serializable {
    @Serial
    private static final long serialVersionUID = -4031445703495430091L;

    private String name;

    private int style;

    private int size;

    public Font creatFont() {
        return new Font(this.name, this.style, this.size);
    }
}
