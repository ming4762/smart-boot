package com.smart.commons.core.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 日期时间工具类
 * @author shizhongming
 * 2020/1/8 8:27 下午
 */
public final class DateUtils {

    private static final String CST_DATE_STR = "CST";

    private static final Pattern YYYY_MM_DD = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}.*");

    private static final Pattern YYYY_M_D = Pattern.compile("^[0-9]{4}-[0-9]{1}-[0-9]+.*||^[0-9]{4}-[0-9]+-[0-9]{1}.*");

    private static final Pattern YY_MM_DD = Pattern.compile("^[0-9]{2}-[0-9]{2}-[0-9]{2}.*");

    private static final Pattern YY_M_D = Pattern.compile("^[0-9]{2}-[0-9]{1}-[0-9]+.*||^[0-9]{2}-[0-9]+-[0-9]{1}.*");

    private static final Pattern HH = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}.*");

    private static final Pattern HH_MM = Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}");

    private static final Pattern HH_MM_SS = Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}");

    private static final Pattern HH_MM_SS_SSS = Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}:[0-9]{0,3}");

    private static final Pattern YYYY_MM_DD_HH_MM_SS_SSS_Z = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{0,3}Z");

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
        final LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        final Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
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
        return DateUtils.batchFormat(dateList, pattern).get(0);
    }

    /**
     * 格式化时间
     * @param localDateTime 时间
     * @param pattern 格式
     * @return 时间字符串
     */
    public static String format(@NonNull LocalDateTime localDateTime, @NonNull String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
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
                    final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    return localDateTime.format(formatter);
                }).collect(Collectors.toList());
    }

    /**
     * 字符串转为时间
     * @param dateStr 字符串
     * @return 时间
     */
    @SneakyThrows(ParseException.class)
    @Nullable
    public static Date convertDate(String dateStr) {
        boolean isUtc = false;
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        if (dateStr.contains(CST_DATE_STR)) {
            return new SimpleDateFormat().parse(dateStr);
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
        if(HH.matcher(dateDealStr).matches()){
            dateFormatStr += " HH";
        }else if(HH_MM.matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm";
        }else if(HH_MM_SS.matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm:ss";
        }else if(HH_MM_SS_SSS.matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm:ss:sss";
        }

        if (YYYY_MM_DD_HH_MM_SS_SSS_Z.matcher(dateDealStr).matches()) {
            dateDealStr = dateDealStr.replace("Z", " UTC");
            dateFormatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";
            isUtc = true;
        }
        if (StringUtils.isNotBlank(dateFormatStr)) {
            final SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
            if (isUtc) {
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            return format.parse(dateDealStr);
        }
        return null;
    }

    /**
     * 获取天时间间隔
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 时间段
     */
    public static List<LocalDate> getBetweenDay(LocalDate startDate, LocalDate endDate) {
        return getBetweenTime(startDate.atStartOfDay(), endDate.atStartOfDay(), ChronoUnit.DAYS, f -> f.plusDays(1))
                .stream().map(LocalDateTime::toLocalDate)
                .collect(Collectors.toList());
    }

    /**
     * 获取时间间隔-周
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 时间段
     */
    public static List<LocalDate> getBetweenWeek(LocalDate startDate, LocalDate endDate) {
        return getBetweenTime(startDate.atStartOfDay(), endDate.atStartOfDay(), ChronoUnit.WEEKS, f -> f.plusWeeks(1))
                .stream().map(LocalDateTime::toLocalDate)
                .collect(Collectors.toList());
    }

    /**
     * 获取时间间隔-月
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 时间段
     */
    public static List<LocalDate> getBetweenMonth(LocalDate startDate, LocalDate endDate) {
        return getBetweenTime(startDate.atStartOfDay(), endDate.atStartOfDay(), ChronoUnit.MONTHS, f -> f.plusMonths(1))
                .stream().map(LocalDateTime::toLocalDate)
                .collect(Collectors.toList());
    }

    public static List<LocalDateTime> getBetweenTime(@NonNull LocalDateTime startTime, @NonNull LocalDateTime endTime, @NonNull Duration duration) {
        if (startTime.isAfter(endTime)) {
            return new ArrayList<>(0);
        }
        List<LocalDateTime> result = new LinkedList<>();
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
    public static List<LocalDateTime> getBetweenTime(@NonNull LocalDateTime startDate, @NonNull LocalDateTime endDate, @NonNull ChronoUnit chronoUnit, @NonNull UnaryOperator<LocalDateTime> unaryOperator) {
        long distance = chronoUnit.between(startDate, endDate);
        List<LocalDateTime> result = new LinkedList<>();
        if (distance < 1) {
            return result;
        }
        Stream.iterate(startDate, unaryOperator)
                .limit(distance + 1)
                .forEach(result :: add);
        return result;
    }

}
