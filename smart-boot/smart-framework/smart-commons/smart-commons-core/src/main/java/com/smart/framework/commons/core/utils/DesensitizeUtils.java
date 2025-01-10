package com.smart.framework.commons.core.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * 脱敏工具类
 * @author shizhongming
 * 2025/1/10 9:35
 * @since 5.0.0
 */
public class DesensitizeUtils {

    private DesensitizeUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String EMAIL_SPLIT = "@";
    private static final String FILL_STR = "*";
    private static final int ID_CARD_LENGTH = 18;
    private static final int MIN_BANK_CARD_LENGTH = 13;
    private static final int MAX_BANK_CARD_LENGTH = 19;

    private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

    /**
     * 邮箱脱敏
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String email(String email) {
        if (!StringUtils.hasText(email) || !email.contains(EMAIL_SPLIT)) {
            return email;
        }
        String[] emailArr = email.split(EMAIL_SPLIT);
        String username = emailArr[0];
        String domain = emailArr[1];

        // 只保留用户名的首字母，剩余部分用*代替
        if (username.length() > 1) {
            username = username.charAt(0) + generateFillStr(4);
        }

        return username +EMAIL_SPLIT + domain;
    }

    /**
     * 对电话号码脱敏
     * @param phone 电话号码
     * @return 脱敏后的电话号码
     */
    @SneakyThrows(NumberParseException.class)
    public static String phone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return "";
        }
        Phonenumber.PhoneNumber phoneNumber = PHONE_NUMBER_UTIL.parse(phone, "CN");
        if (!PHONE_NUMBER_UTIL.isValidNumber(phoneNumber)) {
            return phone;
        }
        String format = PHONE_NUMBER_UTIL.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        String[] parts = format.split(" ");
        parts[2] = generateFillStr(4);
        return String.join(" ", parts);
    }

    /**
     * 对身份证号码脱敏
     * @param idCard 身份证号码
     * @return 脱敏后的身份证号码
     */
    public static String idCard(String idCard) {
        if (!StringUtils.hasText(idCard) || idCard.length() != ID_CARD_LENGTH) {
            return "";
        }

        // 保留前六位和后四位，中间部分替换为*
        String prefix = idCard.substring(0, 6);
        String suffix = idCard.substring(14);

        return prefix + generateFillStr(8) + suffix;
    }

    /**
     * 对银行卡号脱敏
     * @param bankCard 银行卡号
     * @return 脱敏后的银行卡号
     */
    public static String bankCard(String bankCard) {
        if (!StringUtils.hasText(bankCard) || bankCard.length() < MIN_BANK_CARD_LENGTH || bankCard.length() > MAX_BANK_CARD_LENGTH) {
            return "";
        }
        // 保留前四位和后四位，中间部分替换为*
        String prefix = bankCard.substring(0, 6);
        String suffix = bankCard.substring(bankCard.length() - 4);
        return prefix + generateFillStr(bankCard.length() - 10) + suffix;
    }


    /**
     * 构建脱敏后的字符串
     * @param num 脱敏长度
     * @return 脱敏后的字符串
     */
    private static String generateFillStr(int num) {
        if (num <= 0) {
            return "";
        }
        return String.join("", Collections.nCopies(num, FILL_STR));
    }
}
