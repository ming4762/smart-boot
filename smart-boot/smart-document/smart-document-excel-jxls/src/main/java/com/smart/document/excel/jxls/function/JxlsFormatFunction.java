package com.smart.document.excel.jxls.function;

import com.smart.commons.core.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 格式化函数
 * @author ShiZhongMing
 * 2021/8/4 21:51
 * @since 1.0
 */
public class JxlsFormatFunction {

    /**
     * 格式化时间
     * @param date 时间
     * @param formatter 格式化参数
     * @return 格式化后的时间
     */
    public String formatDate(Date date, String formatter) {
        if (date == null) {
            return  "";
        }
        return DateUtils.format(date, formatter);
    }

    /**
     * 格式化时间
     * @param localDateTime 时间
     * @param formatter 格式化参数
     * @return 格式化后的时间
     */
    public String formatLocalDateTime(LocalDateTime localDateTime, String formatter) {
        if (localDateTime == null) {
            return "";
        }
        return DateUtils.format(localDateTime, formatter);
    }
}
