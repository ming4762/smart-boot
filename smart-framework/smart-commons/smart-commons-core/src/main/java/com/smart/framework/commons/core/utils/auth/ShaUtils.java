package com.smart.framework.commons.core.utils.auth;

import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author shizhongming
 * 2023/10/27 16:34
 * @since 3.0.0
 */
public class ShaUtils {

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String HMAC_SHA256 = "HmacSHA256";

    private ShaUtils() {
        // nothing
    }

    /**
     * hmacSha1 加密
     * @param encryptContent 加密内容
     * @param encryptKey 加密值
     * @return 加密后
     */
    public static byte[] hmacSha1Encrypt(String encryptContent, String encryptKey) {
        return hmacShaEncrypt(encryptContent, encryptKey, HMAC_SHA1);
    }

    /**
     * hmacSha256 加密
     * @param encryptContent 加密内容
     * @param encryptKey 加密值
     * @return 加密后
     */
    public static byte[] hmacSha256Encrypt(String encryptContent, String encryptKey) {
        return hmacShaEncrypt(encryptContent, encryptKey, HMAC_SHA256);
    }

    /**
     * hmacSha 加密
     * @param encryptContent 加密内容
     * @param encryptKey 加密值
     * @param algorithm 算法
     * @return 加密后
     */
    @SneakyThrows({NoSuchAlgorithmException.class, InvalidKeyException.class})
    private static byte[] hmacShaEncrypt(String encryptContent, String encryptKey, String algorithm) {
        byte[] keyBytes = encryptKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, algorithm);

        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        byte[] contentBytes = encryptContent.getBytes(StandardCharsets.UTF_8);

        return mac.doFinal(contentBytes);
    }
}
