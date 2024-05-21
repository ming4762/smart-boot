package com.smart.commons.core.dto.common;

import com.smart.commons.core.constants.LabelValueEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/3/19 14:57
 * @since 3.0.0
 */
@Getter
public class LabelValueData<T extends LabelValueEnum> implements Serializable {
    @Serial
    private static final long serialVersionUID = -1035786293869637153L;

    private final String value;

    private final String label;

    @Setter
    private T enumData;

    @Setter
    private Serializable data;

    public LabelValueData(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
