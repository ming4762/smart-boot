package com.smart.framework.commons.core.utils.auth;

import com.smart.framework.commons.core.dto.auth.AuthAkSkCreateTokenDTO;
import com.smart.framework.commons.core.utils.Base64Utils;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * @param parameter 参数
     * @param date 日期GTM格式
     * @return 认证签名
     */
    public static String createSign(AuthAkSkCreateTokenDTO parameter, String date) {
        String encryptKey = Stream.of(
                        parameter.getHttpMethod().name(),
                        parameter.getContentType(),
                        date,
                        parameter.getNonce(),
                        parameter.getParameterStr()
                ).filter(StringUtils::hasText)
                .collect(Collectors.joining(SPLIT));
        String encodeSign = Base64Utils.encode(ShaUtils.hmacSha256Encrypt(parameter.getSecretKey(), encryptKey));
        return String.join(SPLIT, List.of(
                parameter.getPrefix(),
                parameter.getAccessKey(),
                encodeSign
        ));
    }

    /**
     * 获取认证签名
     * @param parameter 参数
     * @param date 日期
     * @return 认证签名
     */
    public static String createSign(AuthAkSkCreateTokenDTO parameter, ZonedDateTime date) {
        ZonedDateTime zonedDateTime = date.withZoneSameInstant(ZoneId.of("GMT"));

        return createSign(parameter, DATE_FORMATTER.format(zonedDateTime));
    }

    /**
     * 获取认证签名时间
     * @param zonedDateTime 日期
     * @return 认证签名时间
     */
    public static String getSignDate(ZonedDateTime zonedDateTime) {
        return DATE_FORMATTER.format(zonedDateTime);
    }
}
