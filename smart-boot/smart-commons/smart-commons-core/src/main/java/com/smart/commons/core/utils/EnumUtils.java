package com.smart.commons.core.utils;

import com.smart.commons.core.constants.LabelValueEnum;
import com.smart.commons.core.dto.common.LabelValueData;

import java.util.Arrays;
import java.util.List;

/**
 * @author shizhongming
 * 2024/3/19 14:56
 * @since 3.0.0
 */
public class EnumUtils {

    /**
     * 解析枚举类，并转为label value 结果
     * @param enumClass 枚举类
     * @return label value
     */
   public static List<LabelValueData> convertLabelValue(Class<? extends LabelValueEnum> enumClass) {
       LabelValueEnum[] enumConstants = enumClass.getEnumConstants();
       if (enumConstants == null) {
           throw new IllegalArgumentException("不是枚举类型，请检查类型");
       }
       return Arrays.stream(enumConstants)
               .map(item -> new LabelValueData(item.getValue(), item.getLabel()))
               .toList();
   }
}
