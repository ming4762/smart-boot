package com.smart.framework.commons.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author shizhongming
 * 2024/12/16 19:47
 * @since 5.0.0
 */
public class PasswordUtils {

    private PasswordUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final Random RANDOM = new Random();
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "%$#@&";

    /**
     * 生产随机密码
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int minLength, int maxLength) {
        // 定义字符池
        String allChars = UPPER_CASE + LOWER_CASE + DIGITS + SPECIAL_CHARS;

        // 确保每种字符至少有一个
        List<Character> result = new ArrayList<>();
        result.add(UPPER_CASE.charAt(RANDOM.nextInt(UPPER_CASE.length())));
        result.add(LOWER_CASE.charAt(RANDOM.nextInt(LOWER_CASE.length())));
        result.add(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        result.add(SPECIAL_CHARS.charAt(RANDOM.nextInt(SPECIAL_CHARS.length())));

        // 随机生成剩余字符
        int remainingLength = RANDOM.nextInt(maxLength - minLength + 1) + minLength - 4;
        for (int i = 0; i < remainingLength; i++) {
            result.add(allChars.charAt(RANDOM.nextInt(allChars.length())));
        }
        // 打乱字符顺序
        Collections.shuffle(result);
        // 转换为字符串
        StringBuilder randomString = new StringBuilder();
        for (char c : result) {
            randomString.append(c);
        }
        return randomString.toString();
    }

    /**
     * 校验密码
     * @param password 密码
     * @return 是否通过
     */
    public static boolean validatePassword(String password, String regex) {
        // 必须包含大小写字母、数字、特殊符号 %$#@&
//        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[%$#@&]).{10,18}$";
        if (!Pattern.matches(regex, password)) {
            return false;
        }
        // 不能包含连续的数字或字母（例如：123，abc）
        return !containsConsecutiveSequence(password);
    }

    private static boolean containsConsecutiveSequence(String password) {
        // 检查是否包含连续的数字
        for (int i = 0; i < password.length() - 2; i++) {
            char first = password.charAt(i);
            char second = password.charAt(i + 1);
            char third = password.charAt(i + 2);
            // 连续数字
            if (Character.isDigit(first) && Character.isDigit(second) && Character.isDigit(third) && second - first == 1 && third - second == 1) {
                return true;
            }
            // 连续字母
            if (Character.isLetter(first) && Character.isLetter(second) && Character.isLetter(third) && second - first == 1 && third - second == 1) {
                return true;
            }
        }
        return false;
    }
}
