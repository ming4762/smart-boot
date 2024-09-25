package com.smart.commons.core.utils.auth;

import com.smart.commons.core.utils.Base64Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * @author shizhongming
 * 2024/9/25 14:15
 * @since 3.0.0
 */
public class SecretUtils {

    private static final String SPLIT = ":";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    private SecretUtils() {
        // nothing
    }

    /**
     * 获取认证签名
     * @param httpMethod 请求方法
     * @param contentType 请求体类型
     * @param date 日期GTM格式
     * @param notice 随机串
     * @param prefix 前缀
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @return 认证签名
     */
    public static String createSign(String httpMethod, String contentType, String date, String notice, String prefix, String accessKey, String secretKey) {
        String encryptKey = String.join(SPLIT, List.of(
                httpMethod,
                contentType,
                date,
                notice
        ));
        String encodeSign = Base64Utils.encode(ShaUtils.hmacSha1Encrypt(secretKey, encryptKey));
        return String.join(SPLIT, List.of(
                prefix,
                accessKey,
                encodeSign
        ));
    }

    /**
     * 获取认证签名
     * @param httpMethod 请求方法
     * @param contentType 请求体类型
     * @param date 日期
     * @param notice 随机串
     * @param prefix 前缀
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @return 认证签名
     */
    public static String createSign(String httpMethod, String contentType, LocalDateTime date, String notice, String prefix, String accessKey, String secretKey) {
        ZonedDateTime zonedDateTime = date.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("GMT"));

        return createSign(httpMethod, contentType, DATE_FORMATTER.format(zonedDateTime), notice, prefix, accessKey, secretKey);
    }
}
