package com.smart.framework.commons.core.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author shizhongming
 * 2025/10/15 18:51
 * @since 5.0.0
 */
class DateUtilsTest {

    @Test
    void testISO8601WithUTC() {
        String dateStr = "2025-10-15T14:30:45.123Z";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(2025, zdt.getYear());
        assertEquals(10, zdt.getMonthValue());
        assertEquals(15, zdt.getDayOfMonth());
        assertEquals(14, zdt.getHour());
        assertEquals(30, zdt.getMinute());
        assertEquals(45, zdt.getSecond());
    }

    @Test
    void testDateOnlyYYYYMMDD() {
        String dateStr = "2025-10-15";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(LocalDate.of(2025, 10, 15), zdt.toLocalDate());
        assertEquals(0, zdt.getHour());
        assertEquals(0, zdt.getMinute());
    }

    @Test
    void testDateOnlyYYYYMD() {
        String dateStr = "2025-1-5";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(LocalDate.of(2025, 1, 5), zdt.toLocalDate());
    }

    @Test
    void testDateWithTimeHHMM() {
        String dateStr = "2025-10-15 14:30";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(14, zdt.getHour());
        assertEquals(30, zdt.getMinute());
        assertEquals(0, zdt.getSecond());
    }

    @Test
    void testDateWithTimeHHMMSS() {
        String dateStr = "2025-10-15 14:30:45";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(14, zdt.getHour());
        assertEquals(30, zdt.getMinute());
        assertEquals(45, zdt.getSecond());
    }

    @Test
    void testDateWithTimeHHMMSSSSS() {
        String dateStr = "2025-10-15 14:30:45:123";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(14, zdt.getHour());
        assertEquals(30, zdt.getMinute());
        assertEquals(45, zdt.getSecond());
        assertEquals(123_000_000, zdt.getNano()); // 纳秒 123ms
    }

    @Test
    void testDateWithChineseFormat() {
        String dateStr = "2025年10月15日";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(LocalDate.of(2025, 10, 15), zdt.toLocalDate());
    }

    @Test
    void testDateWithSlash() {
        String dateStr = "2025/10/15";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(LocalDate.of(2025, 10, 15), zdt.toLocalDate());
    }

    @Test
    void testDateWithDot() {
        String dateStr = "2025.10.15";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(LocalDate.of(2025, 10, 15), zdt.toLocalDate());
    }

    @Test
    void testTwoDigitYear() {
        String dateStr = "25-10-15";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(2025, zdt.getYear());
        assertEquals(10, zdt.getMonthValue());
        assertEquals(15, zdt.getDayOfMonth());
    }

    @Test
    void testEmptyOrNull() {
        assertNull(DateUtils.convertDate(null));
        assertNull(DateUtils.convertDate(""));
        assertNull(DateUtils.convertDate("   "));
    }

    @Test
    void testInvalidDate() {
        assertNull(DateUtils.convertDate("invalid-date"));
        assertNull(DateUtils.convertDate("2025-13-40"));
    }

    @Test
    void testCSTString() {
        String dateStr = "Wed Oct 15 14:30:00 CST 2025";
        ZonedDateTime zdt = DateUtils.convertDate(dateStr);
        assertNotNull(zdt);
        assertEquals(2025, zdt.getYear());
        assertEquals(10, zdt.getMonthValue());
        assertEquals(15, zdt.getDayOfMonth());
    }
}
