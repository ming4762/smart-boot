package com.smart.commons.core.constants;

import java.io.Serializable;

/**
 * 返回label value的枚举
 * @author shizhongming
 * 2024/3/19 14:53
 * @since 3.0.0
 */
public interface LabelValueEnum extends Serializable {

    /**
     * 获取value
     * @return value
     */
    String getValue();

    /**
     * 获取label
     * @return label
     */
    String getLabel();
}
