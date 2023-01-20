package com.smart.commons.core.utils.auth;

import com.smart.commons.core.utils.Base64Utils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA工具类
 * @author ShiZhongMing
 * 2022/8/9
 * @since 3.0.0
 */
public class RsaUtils {

    private RsaUtils() {
        // nothing
    }

    /**
     * 秘钥算法
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 秘钥长度
     */
    private static final int KEY_SIZE = 2048;

    /**
     * 随机生成秘钥对
     * @return 秘钥对
     */
    @SneakyThrows(NoSuchAlgorithmException.class)
    public static KeyPair generateKeyPair() {
        return generateKeyPair(ALGORITHM);
    }

    @SneakyThrows
    public static KeyPair generateKeyPair(String algorithm) {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(KEY_SIZE);

        return generator.generateKeyPair();
    }

    /**
     * 将key写入文件
     * @param key key
     * @param outputStream 输出流
     */
    @SneakyThrows(IOException.class)
    public static void saveKey(@NonNull Key key, OutputStream outputStream) {
        var encoded = key.getEncoded();

        String base64 = Base64Utils.encode(encoded);

        IOUtils.write(base64, outputStream, StandardCharsets.UTF_8);
    }

    /**
     * 保存秘钥对
     * @param keyPair 秘钥对
     * @param pubOut 公钥输出流
     * @param priOut 私钥输出流
     */
    public static void saveKeyPair(@NonNull KeyPair keyPair, OutputStream pubOut, OutputStream priOut) {
        saveKey(keyPair.getPublic(), pubOut);
        saveKey(keyPair.getPrivate(), priOut);
    }

    /**
     * 生成并保存秘钥
     * @param pubOut 公钥输出流
     * @param priOut 私钥输出流
     */
    public static void generateSaveKeyPair(OutputStream pubOut, OutputStream priOut) {
        saveKeyPair(generateKeyPair(), pubOut, priOut);
    }

    /**
     * 生成公钥
     * @param pubKeyBase64 公钥文本
     * @return 公钥
     */
    @SneakyThrows({NoSuchAlgorithmException.class, InvalidKeySpecException.class})
    public static PublicKey generaPublicKey(String pubKeyBase64) {
        var pubKeyByte = Base64Utils.decode(pubKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyByte);

        return KeyFactory.getInstance(ALGORITHM).generatePublic(keySpec);
    }

    @SneakyThrows(IOException.class)
    public static PublicKey generaPublicKey(InputStream inputStream) {
        return generaPublicKey(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    @SneakyThrows(IOException.class)
    public static PrivateKey generaPrivateKey(InputStream inputStream) {
        return generaPrivateKey(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    /**
     * 生成私钥
     * @param priKeyBase64 私钥文本
     * @return 私钥
     */
    @SneakyThrows({NoSuchAlgorithmException.class, InvalidKeySpecException.class})
    public static PrivateKey generaPrivateKey(String priKeyBase64) {
        var pubKeyByte = Base64Utils.decode(priKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pubKeyByte);

        return KeyFactory.getInstance(ALGORITHM).generatePrivate(keySpec);
    }

    /**
     * 公钥加密
     * @param data 需要加密的数据
     * @param publicKey 公钥
     * @return 加密后的数据
     */
    @SneakyThrows
    public static byte[] publicEncrypt(String data, RSAPublicKey publicKey) {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 私钥加密
     * @param data 需要加密的数据
     * @param privateKey 私钥
     * @return 加密后的数据
     */
    @SneakyThrows
    public static byte[] privateEncrypt(String data, RSAPrivateKey privateKey) {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 公钥解密
     * @param data 需要解密的数据
     * @param publicKey 公钥
     * @return 加密后的数据
     */
    @SneakyThrows
    public static byte[] publicDecrypt(String data, RSAPublicKey publicKey) {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 私钥解密
     * @param data 需要解密的数据
     * @param privateKey 私钥
     * @return 加密后的数据
     */
    @SneakyThrows
    public static byte[] privateDecrypt(String data, RSAPrivateKey privateKey) {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }
}
