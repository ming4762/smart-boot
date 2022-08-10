package com.smart.commons.core.utils;

import com.smart.commons.core.constants.MessageDigestAlgorithms;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * MessageDigest 工具类
 * @author ShiZhongMing
 * 2022/5/18
 * @since 2.0.0
 */
public final class DigestUtils {

    private DigestUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通用加密函数
     * @param algorithms 加密方法
     * @param value 加密的值
     * @param hashIterations 加密次数
     * @return 加密的值
     */
    public static String digest(@NonNull MessageDigestAlgorithms algorithms, @NonNull String value, @NonNull Integer hashIterations) {
        MessageDigest messageDigest = org.apache.commons.codec.digest.DigestUtils.getDigest(algorithms.getAlgorithms());
        byte[] hash = org.apache.commons.codec.digest.DigestUtils.digest(messageDigest, StringUtils.getBytesUtf8(value));
        if (hashIterations > 1) {
            for(int i = 1; i<hashIterations; i++) {
                hash = org.apache.commons.codec.digest.DigestUtils.digest(messageDigest, hash);
            }
        }
        return Hex.encodeHexString(hash);
    }

    /**
     * 通用加密函数
     * @param algorithms 加密方法
     * @param inputStream 输入流
     * @param hashIterations 加密次数
     * @return 加密的值
     */
    @SneakyThrows(IOException.class)
    public static String digest(MessageDigestAlgorithms algorithms, @NonNull InputStream inputStream, @NonNull Integer hashIterations) {
        MessageDigest messageDigest = org.apache.commons.codec.digest.DigestUtils.getDigest(algorithms.getAlgorithms());
        byte[] hash = org.apache.commons.codec.digest.DigestUtils.digest(messageDigest, inputStream);
        if (hashIterations > 1) {
            for(int i = 1; i<hashIterations; i++) {
                hash = org.apache.commons.codec.digest.DigestUtils.digest(messageDigest, hash);
            }
        }
        return Hex.encodeHexString(hash);
    }

    /**
     * SHA256加密
     * @param inputStream 输入流
     * @return 加密的值
     */
    public static String sha256(@NonNull InputStream inputStream) {
        return sha256(inputStream, 1);
    }

    /**
     * SHA256加密
     * @param inputStream 输入流
     * @param hashIterations 加密次数
     * @return 加密的值
     */
    public static String sha256(@NonNull InputStream inputStream, @NonNull Integer hashIterations) {
        return digest(MessageDigestAlgorithms.SHA_256, inputStream, hashIterations);
    }

    /**
     * SHA256加密
     * @param value 加密的值
     * @param hashIterations 加密次数
     * @return 加密的值
     */
    public static String sha256(@NonNull String value, @NonNull Integer hashIterations) {
        return digest(MessageDigestAlgorithms.SHA_256, value, hashIterations);
    }
}
