package com.smart.framework.commons.core.utils;

import com.smart.framework.commons.core.constants.LabelValueEnum;
import com.smart.framework.commons.core.dto.common.LabelValueData;

import java.util.Arrays;
import java.util.List;

/**
 * @author shizhongming
 * 2024/3/19 14:56
 * @since 3.0.0
 */
public class EnumUtils {

    private EnumUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 解析枚举类，并转为label value 结果
     * @param enumClass 枚举类
     * @return label value
     */
   public static <T extends LabelValueEnum> List<LabelValueData<T>> convertLabelValue(Class<T> enumClass) {
       T[] enumConstants = enumClass.getEnumConstants();
       if (enumConstants == null) {
           throw new IllegalArgumentException("不是枚举类型，请检查类型");
       }
       return Arrays.stream(enumConstants)
               .map(item -> {
                   LabelValueData<T> valueData = new LabelValueData<>(item.getValue(), item.getLabel());
                   valueData.setEnumData(item);
                   return valueData;
               })
               .toList();
   }
}
