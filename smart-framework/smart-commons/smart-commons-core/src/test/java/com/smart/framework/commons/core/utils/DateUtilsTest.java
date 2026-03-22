package com.smart.framework.commons.core.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DateUtils 单元测试
 */
@DisplayName("DateUtils 工具类测试")
class DateUtilsTest {

    // ----------------------------------------------------------------
    // parse(String, String)
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("parse(String dateStr, String pattern)")
    class ParseTest {

        @Test
        @DisplayName("合法日期字符串解析成功")
        void parse_validDateString_returnsDate() {
            String dateStr = "2024-06-15 10:30:00 +0800";
            String pattern = "yyyy-MM-dd HH:mm:ss Z";
            Date result = DateUtils.parse(dateStr, pattern);
            assertNotNull(result);
        }

        @Test
        @DisplayName("解析结果与预期 Instant 一致")
        void parse_correctInstant() {
            String dateStr = "2024-01-01T00:00:00Z";
            String pattern = "yyyy-MM-dd'T'HH:mm:ssX";
            Date result = DateUtils.parse(dateStr, pattern);
            assertEquals(Instant.parse("2024-01-01T00:00:00Z"), result.toInstant());
        }

        @Test
        @DisplayName("非法格式抛出异常")
        void parse_invalidPattern_throwsException() {
            assertThrows(Exception.class, () -> DateUtils.parse("not-a-date", "yyyy-MM-dd"));
        }
    }

    // ----------------------------------------------------------------
    // format(Date, String)
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("format(Date date, String pattern)")
    class FormatDateTest {

        @Test
        @DisplayName("格式化为 yyyy-MM-dd")
        void format_date_yyyyMMdd() {
            ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 0, 0, 0, 0, ZoneId.systemDefault());
            Date date = Date.from(zdt.toInstant());
            String result = DateUtils.format(date, "yyyy-MM-dd");
            assertEquals("2024-06-15", result);
        }

        @Test
        @DisplayName("格式化为 yyyy/MM/dd HH:mm:ss")
        void format_date_fullPattern() {
            ZonedDateTime zdt = ZonedDateTime.of(2024, 12, 31, 23, 59, 59, 0, ZoneId.systemDefault());
            Date date = Date.from(zdt.toInstant());
            String result = DateUtils.format(date, "yyyy/MM/dd HH:mm:ss");
            assertEquals("2024/12/31 23:59:59", result);
        }
    }

    // ----------------------------------------------------------------
    // format(ZonedDateTime, String)
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("format(ZonedDateTime, String)")
    class FormatZonedDateTimeTest {

        @Test
        @DisplayName("ZonedDateTime 格式化")
        void format_zonedDateTime_pattern() {
            ZonedDateTime zdt = ZonedDateTime.of(2024, 3, 8, 9, 5, 3, 0, ZoneId.of("UTC"));
            String result = DateUtils.format(zdt, "yyyy-MM-dd HH:mm:ss");
            assertEquals("2024-03-08 09:05:03", result);
        }
    }

    // ----------------------------------------------------------------
    // format(ZonedDateTime, String, ZoneId)
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("format(ZonedDateTime, String, ZoneId)")
    class FormatWithZoneIdTest {

        @Test
        @DisplayName("UTC 时间转为 Asia/Shanghai 显示")
        void format_convertTimezone() {
            ZonedDateTime utc = ZonedDateTime.of(2024, 6, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
            String result = DateUtils.format(utc, "yyyy-MM-dd HH:mm:ss", ZoneId.of("Asia/Shanghai"));
            // UTC+8，应显示 08:00:00
            assertEquals("2024-06-01 08:00:00", result);
        }

        @Test
        @DisplayName("相同时区转换结果不变")
        void format_sameZone_unchanged() {
            ZoneId zone = ZoneId.of("Asia/Shanghai");
            ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 1, 12, 30, 0, 0, zone);
            String result = DateUtils.format(zdt, "HH:mm:ss", zone);
            assertEquals("12:30:00", result);
        }
    }

    // ----------------------------------------------------------------
    // batchFormat
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("batchFormat(List<Date>, String)")
    class BatchFormatTest {

        @Test
        @DisplayName("批量格式化返回等长列表且内容正确")
        void batchFormat_sameSize() {
            ZonedDateTime base = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
            List<Date> dates = List.of(
                    Date.from(base.toInstant()),
                    Date.from(base.plusDays(1).toInstant()),
                    Date.from(base.plusDays(2).toInstant())
            );
            List<String> result = DateUtils.batchFormat(dates, "yyyy-MM-dd");
            assertEquals(3, result.size());
            assertEquals("2024-01-01", result.get(0));
            assertEquals("2024-01-02", result.get(1));
            assertEquals("2024-01-03", result.get(2));
        }

        @Test
        @DisplayName("空列表返回空结果")
        void batchFormat_emptyList() {
            List<String> result = DateUtils.batchFormat(List.of(), "yyyy-MM-dd");
            assertTrue(result.isEmpty());
        }
    }

    // ----------------------------------------------------------------
    // convertDate
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("convertDate(String)")
    class ConvertDateTest {

        @ParameterizedTest
        @DisplayName("null 或空字符串返回 null")
        @NullAndEmptySource
        void convertDate_nullOrEmpty_returnsNull(String input) {
            assertNull(DateUtils.convertDate(input));
        }

        @Test
        @DisplayName("空白字符串返回 null")
        void convertDate_blank_returnsNull() {
            assertNull(DateUtils.convertDate("   "));
        }

        @ParameterizedTest
        @DisplayName("标准 yyyy-MM-dd 格式解析")
        @CsvSource({
                "2024-06-15, 2024, 6, 15",
                "2000-01-01, 2000, 1, 1",
                "1999-12-31, 1999, 12, 31"
        })
        void convertDate_yyyyMMdd(String input, int year, int month, int day) {
            ZonedDateTime result = DateUtils.convertDate(input);
            assertNotNull(result);
            assertEquals(year,  result.getYear());
            assertEquals(month, result.getMonthValue());
            assertEquals(day,   result.getDayOfMonth());
        }

        @Test
        @DisplayName("yyyy-M-d 单位数月日 + 时分解析")
        void convertDate_yyyyMd_withTime() {
            ZonedDateTime result = DateUtils.convertDate("2024-6-5 08:30");
            assertNotNull(result);
            assertAll(
                    () -> assertEquals(2024, result.getYear()),
                    () -> assertEquals(6,    result.getMonthValue()),
                    () -> assertEquals(5,    result.getDayOfMonth()),
                    () -> assertEquals(8,    result.getHour()),
                    () -> assertEquals(30,   result.getMinute())
            );
        }

        @Test
        @DisplayName("中文年月日解析")
        void convertDate_chineseFormat() {
            ZonedDateTime result = DateUtils.convertDate("2024年06月15日");
            assertNotNull(result);
            assertEquals(2024, result.getYear());
            assertEquals(6,    result.getMonthValue());
            assertEquals(15,   result.getDayOfMonth());
        }

        @Test
        @DisplayName("斜杠分隔符解析")
        void convertDate_slashSeparator() {
            ZonedDateTime result = DateUtils.convertDate("2024/06/15");
            assertNotNull(result);
            assertEquals(2024, result.getYear());
        }

        @Test
        @DisplayName("ISO-8601 带 Z 时区解析为 UTC")
        void convertDate_isoInstantFormat() {
            ZonedDateTime result = DateUtils.convertDate("2024-06-15T10:30:00Z");
            assertNotNull(result);
            assertEquals(ZoneId.of("UTC"), result.getZone());
            assertEquals(10, result.getHour());
        }

        @Test
        @DisplayName("带毫秒 ISO-8601 格式解析")
        void convertDate_isoInstantWithMillis() {
            ZonedDateTime result = DateUtils.convertDate("2024-06-15T10:30:00.123Z");
            assertNotNull(result);
            assertEquals(ZoneId.of("UTC"), result.getZone());
        }

        @Test
        @DisplayName("带时分秒的完整时间解析")
        void convertDate_withHHMMSS() {
            ZonedDateTime result = DateUtils.convertDate("2024-06-15 12:30:45");
            assertNotNull(result);
            assertAll(
                    () -> assertEquals(12, result.getHour()),
                    () -> assertEquals(30, result.getMinute()),
                    () -> assertEquals(45, result.getSecond())
            );
        }

        @Test
        @DisplayName("无法识别的格式返回 null")
        void convertDate_unrecognizedFormat_returnsNull() {
            assertNull(DateUtils.convertDate("not-a-date-at-all"));
        }
    }

    // ----------------------------------------------------------------
    // getBetweenDay
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("getBetweenDay(ZonedDateTime, ZonedDateTime)")
    class GetBetweenDayTest {

        @Test
        @DisplayName("相差 3 天返回 4 个日期（含首尾）")
        void getBetweenDay_threeDays() {
            ZoneId zone = ZoneId.systemDefault();
            ZonedDateTime start = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, zone);
            ZonedDateTime end   = ZonedDateTime.of(2024, 1, 4, 0, 0, 0, 0, zone);
            List<LocalDate> result = DateUtils.getBetweenDay(start, end);
            assertEquals(4, result.size());
            assertEquals(LocalDate.of(2024, 1, 1), result.get(0));
            assertEquals(LocalDate.of(2024, 1, 4), result.get(3));
        }

        @Test
        @DisplayName("同一天返回空列表")
        void getBetweenDay_sameDay_emptyList() {
            ZonedDateTime dt = ZonedDateTime.of(2024, 6, 1, 0, 0, 0, 0, ZoneId.systemDefault());
            assertTrue(DateUtils.getBetweenDay(dt, dt).isEmpty());
        }

        @Test
        @DisplayName("结束时间早于开始时间返回空列表")
        void getBetweenDay_endBeforeStart_emptyList() {
            ZoneId zone = ZoneId.systemDefault();
            ZonedDateTime start = ZonedDateTime.of(2024, 6, 10, 0, 0, 0, 0, zone);
            ZonedDateTime end   = ZonedDateTime.of(2024, 6, 1,  0, 0, 0, 0, zone);
            assertTrue(DateUtils.getBetweenDay(start, end).isEmpty());
        }
    }

    // ----------------------------------------------------------------
    // getBetweenWeek
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("getBetweenWeek(ZonedDateTime, ZonedDateTime)")
    class GetBetweenWeekTest {

        @Test
        @DisplayName("相差 2 周返回 3 个节点")
        void getBetweenWeek_twoWeeks() {
            ZoneId zone = ZoneId.systemDefault();
            ZonedDateTime start = ZonedDateTime.of(2024, 1, 1,  0, 0, 0, 0, zone);
            ZonedDateTime end   = ZonedDateTime.of(2024, 1, 15, 0, 0, 0, 0, zone);
            assertEquals(3, DateUtils.getBetweenWeek(start, end).size());
        }
    }

    // ----------------------------------------------------------------
    // getBetweenMonth
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("getBetweenMonth(ZonedDateTime, ZonedDateTime)")
    class GetBetweenMonthTest {

        @Test
        @DisplayName("相差 3 个月返回 4 个节点")
        void getBetweenMonth_threeMonths() {
            ZoneId zone = ZoneId.systemDefault();
            ZonedDateTime start = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, zone);
            ZonedDateTime end   = ZonedDateTime.of(2024, 4, 1, 0, 0, 0, 0, zone);
            List<LocalDate> result = DateUtils.getBetweenMonth(start, end);
            assertEquals(4, result.size());
            assertEquals(1, result.get(0).getMonthValue());
            assertEquals(4, result.get(3).getMonthValue());
        }
    }

    // ----------------------------------------------------------------
    // getBetweenTime(ZonedDateTime, ZonedDateTime, Duration)
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("getBetweenTime(ZonedDateTime, ZonedDateTime, Duration)")
    class GetBetweenTimeDurationTest {

        @Test
        @DisplayName("以小时为步长，返回正确节点数（不含终点）")
        void getBetweenTime_byHours() {
            ZoneId zone = ZoneId.systemDefault();
            ZonedDateTime start = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, zone);
            ZonedDateTime end   = ZonedDateTime.of(2024, 1, 1, 3, 0, 0, 0, zone);
            // 00:00, 01:00, 02:00 — 不含 end
            assertEquals(3, DateUtils.getBetweenTime(start, end, Duration.ofHours(1)).size());
        }

        @Test
        @DisplayName("开始时间晚于结束时间返回空列表")
        void getBetweenTime_startAfterEnd_empty() {
            ZoneId zone = ZoneId.systemDefault();
            ZonedDateTime start = ZonedDateTime.of(2024, 1, 2, 0, 0, 0, 0, zone);
            ZonedDateTime end   = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, zone);
            assertTrue(DateUtils.getBetweenTime(start, end, Duration.ofHours(1)).isEmpty());
        }
    }

    // ----------------------------------------------------------------
    // parseInstant
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("parseInstant(String)")
    class ParseInstantTest {

        @Test
        @DisplayName("合法 ISO Instant 字符串解析成功")
        void parseInstant_valid() {
            String isoStr = "2024-06-15T10:30:00Z";
            assertEquals(Instant.parse(isoStr), DateUtils.parseInstant(isoStr));
        }

        @Test
        @DisplayName("带毫秒的 ISO Instant 解析")
        void parseInstant_withMillis() {
            String isoStr = "2024-06-15T10:30:00.500Z";
            assertEquals(Instant.parse(isoStr), DateUtils.parseInstant(isoStr));
        }

        @Test
        @DisplayName("非法字符串抛出异常")
        void parseInstant_invalid_throwsException() {
            assertThrows(Exception.class, () -> DateUtils.parseInstant("not-instant"));
        }
    }

    // ----------------------------------------------------------------
    // zonedDateTimeToDate
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("zonedDateTimeToDate(ZonedDateTime)")
    class ZonedDateTimeToDateTest {

        @Test
        @DisplayName("转换结果 Instant 与原始一致")
        void zonedDateTimeToDate_correctInstant() {
            ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 12, 0, 0, 0, ZoneId.of("UTC"));
            Date result = DateUtils.zonedDateTimeToDate(zdt);
            assertEquals(zdt.toInstant(), result.toInstant());
        }

        @Test
        @DisplayName("null 入参抛出 NullPointerException")
        void zonedDateTimeToDate_null_throwsNPE() {
            assertThrows(NullPointerException.class, () -> DateUtils.zonedDateTimeToDate(null));
        }
    }
}