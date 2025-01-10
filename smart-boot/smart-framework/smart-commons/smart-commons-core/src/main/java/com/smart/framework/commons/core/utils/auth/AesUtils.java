package com.smart.framework.commons.core.utils.auth;

import com.smart.framework.commons.core.utils.Base64Utils;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author shizhongming
 * 2025/1/10 14:29
 * @since 5.0.0
 */
public class AesUtils {

    private AesUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String ALGORITHM = "AES";

    /**
     * AES加密
     * @param data 待加密数据
     * @param key 密钥
     * @return 加密后的数据
     */
    @SneakyThrows(Exception.class)
    public static String encrypt(String data, String key) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64Utils.encode(bytes);
    }

    /**
     * AES解密
     * @param data 待解密数据
     * @param key 密钥
     * @return 解密后的数据
     */
    @SneakyThrows(Exception.class)
    public static String decrypt(String data, String key) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedData = Base64Utils.decode(data);
        byte[] originalData = cipher.doFinal(decodedData);
        return new String(originalData, StandardCharsets.UTF_8);
    }
}
