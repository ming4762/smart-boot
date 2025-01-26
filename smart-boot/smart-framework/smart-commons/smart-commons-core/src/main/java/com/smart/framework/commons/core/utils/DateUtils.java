package com.smart.framework.commons.core.utils;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 日期时间工具类
 * @author shizhongming
 * 2020/1/8 8:27 下午
 */
public final class DateUtils {

    private static final String CST_DATE_STR = "CST";

    private static final Pattern YYYY_MM_DD = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$");

    private static final Pattern YYYY_M_D = Pattern.compile("^[0-9]{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01]).*$");

    private static final Pattern YY_MM_DD = Pattern.compile("^([0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])(.*)?$");

    private static final Pattern YY_M_D = Pattern.compile("^[0-9]{2}-[0-9]{1}-[0-9]+.*||^[0-9]{2}-[0-9]+-[0-9]{1}.*");

    private static final Pattern HH = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}.*");

    private static final Pattern HH_MM = Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}");

    private static final Pattern HH_MM_SS = Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}");

    private static final Pattern HH_MM_SS_SSS = Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}:[0-9]{0,3}");

    private static final Pattern YYYY_MM_DD_HH_MM_SS_SSS_Z = Pattern.compile("^(?:19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)(\\.\\d{1,3})?Z$");

    private static final DateTimeFormatter ISO_INSTANT_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date
     */
    @NonNull
    public static Date parse(@NonNull String dateStr, @NonNull String pattern) {
        final ZonedDateTime zonedDateTime = java.time.ZonedDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    /**
     * 格式化时间
     * @param date 时间
     * @param pattern 格式
     * @return 时间字符串
     */
    @NonNull
    public static String format(@NonNull Date date, @NonNull String pattern) {
        List<Date> dateList = new ArrayList<>(1);
        dateList.add(date);
        return DateUtils.batchFormat(dateList, pattern).getFirst();
    }

    /**
     * 格式化时间
     * @param zonedDateTime 时间
     * @param pattern 格式
     * @return 时间字符串
     */
    public static String format(@NonNull ZonedDateTime zonedDateTime, @NonNull String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(zonedDateTime);
    }

    /**
     * 批量格式化时间
     * @param dateList 时间集合
     * @param pattern 格式
     * @return 时间字符串
     */
    public static List<String> batchFormat(@NonNull List<Date> dateList, @NonNull String pattern) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateList
                .stream()
                .map(date -> {
                    final Instant instant = date.toInstant();
                    final ZonedDateTime zonedDateTime = java.time.ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
                    return zonedDateTime.format(formatter);
                }).toList();
    }

    /**
     * 字符串转为时间
     * @param dateStr 字符串
     * @return 时间
     */
    @Nullable
    public static ZonedDateTime convertDate(String dateStr) {
        boolean isTime = false;
        if (!org.springframework.util.StringUtils.hasText(dateStr)) {
            return null;
        }
        if (dateStr.contains(CST_DATE_STR)) {
            return ZonedDateTime.parse(dateStr);
        }
        String dateDealStr = dateStr.replace("年", "-").replace("月", "-").replace("日", "")
                .replace("/", "-").replace("\\.", "-").trim();
        String dateFormatStr = "";
        // 确定日期个税
        if (YYYY_MM_DD.matcher(dateDealStr).matches()) {
            dateFormatStr = "yyyy-MM-dd";
        } else if (YYYY_M_D.matcher(dateDealStr).matches()) {
            dateFormatStr = "yyyy-M-d";
        } else if (YY_MM_DD.matcher(dateDealStr).matches()) {
            dateFormatStr = "yy-MM-dd";
        } else if (YY_M_D.matcher(dateDealStr).matches()) {
            dateFormatStr = "yy-M-d";
        }

        //确定时间格式
        if(HH_MM.matcher(dateDealStr).matches()){
            isTime = true;
            dateFormatStr += " HH:mm";
        }else if(HH_MM_SS.matcher(dateDealStr).matches()){
            isTime = true;
            dateFormatStr += " HH:mm:ss";
        }else if(HH_MM_SS_SSS.matcher(dateDealStr).matches()){
            isTime = true;
            dateFormatStr += " HH:mm:ss:sss";
        }

        if (YYYY_MM_DD_HH_MM_SS_SSS_Z.matcher(dateDealStr).matches()) {
            dateDealStr = dateDealStr.replace("Z", " UTC");
            dateFormatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";
        }
        if (StringUtils.hasText(dateFormatStr)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatStr);
            if (isTime) {
                return ZonedDateTime.parse(dateDealStr, formatter);
            } else {
                return LocalDate.parse(dateDealStr, formatter).atStartOfDay().atZone(ZoneId.systemDefault());
            }

        }
        return null;
    }

    /**
     * 获取天时间间隔
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 时间段
     */
    public static List<LocalDate> getBetweenDay(ZonedDateTime startDate, ZonedDateTime endDate) {
        return getBetweenTime(startDate, endDate, ChronoUnit.DAYS, f -> f.plusDays(1))
                .stream().map(ZonedDateTime::toLocalDate)
                .toList();
    }

    /**
     * 获取时间间隔-周
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 时间段
     */
    public static List<LocalDate> getBetweenWeek(ZonedDateTime startDate, ZonedDateTime endDate) {
        return getBetweenTime(startDate, endDate, ChronoUnit.WEEKS, f -> f.plusWeeks(1))
                .stream().map(ZonedDateTime::toLocalDate)
                .toList();
    }

    /**
     * 获取时间间隔-月
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 时间段
     */
    public static List<LocalDate> getBetweenMonth(ZonedDateTime startDate, ZonedDateTime endDate) {
        return getBetweenTime(startDate, endDate, ChronoUnit.MONTHS, f -> f.plusMonths(1))
                .stream().map(ZonedDateTime::toLocalDate)
                .toList();
    }

    public static List<ZonedDateTime> getBetweenTime(@NonNull ZonedDateTime startTime, @NonNull ZonedDateTime endTime, @NonNull Duration duration) {
        if (startTime.isAfter(endTime)) {
            return new ArrayList<>(0);
        }
        List<ZonedDateTime> result = new LinkedList<>();
        while (startTime.isBefore(endTime)) {
            result.add(startTime);
            startTime = startTime.plus(duration);
        }
        return result;
    }


    /**
     * 获取间隔时间
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param chronoUnit date periods units
     * @param unaryOperator 操作
     * @return 时间段
     */
    public static List<ZonedDateTime> getBetweenTime(@NonNull ZonedDateTime startDate, @NonNull ZonedDateTime endDate, @NonNull ChronoUnit chronoUnit, @NonNull UnaryOperator<ZonedDateTime> unaryOperator) {
        long distance = chronoUnit.between(startDate, endDate);
        List<ZonedDateTime> result = new LinkedList<>();
        if (distance < 1) {
            return result;
        }
        Stream.iterate(startDate, unaryOperator)
                .limit(distance + 1)
                .forEach(result :: add);
        return result;
    }

    /**
     * 将instant ISO字符串转为 Instant
     * @param formatStr instant ISO字符串
     * @return Instant
     */
    public static Instant parseInstant(@NonNull String formatStr) {
        return ISO_INSTANT_DATE_TIME_FORMATTER.parse(formatStr, Instant :: from);
    }

    /**
     * ZonedDateTime转Date
     * @param zonedDateTime zonedDateTime
     * @return Date
     */
    public static Date ZonedDateTimeToDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

}
