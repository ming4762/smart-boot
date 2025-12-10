package com.smart.smc.inter.qingdaoport.auth;

import com.smart.framework.commons.core.utils.Base64Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 云港通签名工具类
 * @author shizhongming
 * 2024/7/2 10:32
 */
@Slf4j
public class SignUtils {

    private SignUtils() {
        throw new UnsupportedOperationException("SignUtils 不能实例化");
    }

    private static final String RSA_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串
     *
     * @param paraMap
     *            要排序的Map对象
     * @param urlEncode
     *            是否需要URLENCODE
     * @param keyToLower
     *            是否需要将Key转换为全小写 true:key转化成小写，false:不转化
     * @return 排序后的参数字符串
     */
    public static String formatUrlMap(Map<String, Object> paraMap, boolean urlEncode, boolean keyToLower) {
        String buff = "";
        List<Map.Entry<String, Object>> infoIds = new ArrayList<>(paraMap.entrySet());
        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        infoIds.sort(Map.Entry.comparingByKey());
        // 构造URL 键值对的格式
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, Object> item : infoIds) {
            if (org.springframework.util.StringUtils.hasText(item.getKey())) {
                String key = item.getKey();
                String val = item.getValue() == null ? "" : item.getValue().toString();
                if (urlEncode) {
                    val = URLEncoder.encode(val, StandardCharsets.UTF_8);
                }
                if (keyToLower) {
                    buf.append(key.toLowerCase()).append("=").append(val);
                } else {
                    buf.append(key).append("=").append(val);
                }
                buf.append("&");
            }
        }
        buff = buf.toString();
        if (!buff.isEmpty()) {
            buff = buff.substring(0, buff.length() - 1);
        }
        return buff;
    }

    /**
     * RSA签名
     * @param content 签名内容
     * @param privateKey  私钥
     * @param charset 内容编码
     * @return 签名
     */
    @SneakyThrows(Exception.class)
    public static String sign(String content, String privateKey, Charset charset) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64Utils.decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(priKey);
        signature.update(content.getBytes(charset));
        byte[] sign = signature.sign();
        return Base64Utils.encode(sign);
    }
}
