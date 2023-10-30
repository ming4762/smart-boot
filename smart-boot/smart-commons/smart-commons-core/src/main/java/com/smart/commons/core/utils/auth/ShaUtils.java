package com.smart.commons.core.utils.auth;

import com.smart.commons.core.utils.Base64Utils;
import com.smart.commons.core.utils.IdGenerator;
import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author shizhongming
 * 2023/10/27 16:34
 * @since 3.0.0
 */
public class ShaUtils {

    private static final String HMAC_SHA1 = "HmacSHA1";

    private ShaUtils() {
        // nothing
    }

    /**
     * hmacSha1 加密
     * @param encryptContent 加密内容
     * @param encryptKey 加密值
     * @return 加密后
     */
    @SneakyThrows({NoSuchAlgorithmException.class, InvalidKeyException.class})
    public static byte[] hmacSha1Encrypt(String encryptContent, String encryptKey) {

        byte[] keyBytes = encryptKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, HMAC_SHA1);

        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(secretKeySpec);
        byte[] contentBytes = encryptContent.getBytes(StandardCharsets.UTF_8);

        return mac.doFinal(contentBytes);
    }
}
