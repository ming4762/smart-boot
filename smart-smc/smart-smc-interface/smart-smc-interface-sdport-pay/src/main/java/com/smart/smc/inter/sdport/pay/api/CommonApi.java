package com.smart.smc.inter.sdport.pay.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.log.LogSourceEnum;
import com.smart.framework.commons.core.utils.*;
import com.smart.framework.commons.core.utils.auth.RsaUtils;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.SysParameterApi;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import com.smart.smc.inter.sdport.pay.constants.NotifyUrlEnum;
import com.smart.smc.inter.sdport.pay.constants.RequestSourceEnum;
import com.smart.smc.inter.sdport.pay.constants.SdportPayUrlEnum;
import com.smart.smc.inter.sdport.pay.exception.SdportPayException;
import com.smart.smc.inter.sdport.pay.pojo.result.SdportPayCommonResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2024/10/29 13:45
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class CommonApi {

    private static final String PLATFORM = "SDPORT_PAY";
    private static final String LOG_IDENT = "30";
    private static final String SIGNATURE = "SHA256withRSA";

    private static final String PUBLIC_KEY = "RSA_PLAT_PUBLIC_KEY";

    private static final String PRIVATE_KEY = "RSA_MER_PRI_KEY";

    private static final String PAY_BASE_URL_PARAM = "PAY_BASE_URL";

    /**
     * 后台通知地址参数
     */
    private static final String NOTIFY_BASE_URL = "NOTIFY_BASE_URL";

    protected final SysLogApi sysLogApi;

    protected final SysParameterApi sysParameterApi;

    public <T> T doRequest(SdportPayUrlEnum url, Object parameter, TypeReference<T> typeReference) {
        return this.doRequest(url, parameter, typeReference, false);
    }

    public <T> T doRequest(SdportPayUrlEnum url, Object parameter, TypeReference<T> typeReference, boolean isDirect) {
        String parameterJson = this.createParameter(parameter);
        String requestUrl = this.getRequestUrl(url);

        log.info("{}，请求URL：{}，参数：{}", url.getRemark(), requestUrl, parameterJson);

        SysLogSaveDTO.SysLogSaveDTOBuilder logBuilder = SysLogSaveDTO.builder()
                .operation(url.getRemark())
                .params(parameterJson)
                .requestPath(requestUrl)
                .platform(PLATFORM)
                .logSource(LogSourceEnum.MANUAL)
                .ident(LOG_IDENT)
                .createUserId(AuthUtils.getCurrentUserId())
                .statusCode(200)
                .createBy(AuthUtils.getCurrentFullName());
        long startTime = System.nanoTime();

        try {
            String body = RestUtils.rest(
                    requestUrl,
                    HttpMethod.POST,
                    Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE),
                    parameterJson,
                    new ParameterizedTypeReference<String>() {
                    },
                    null
            );

            log.info("{}，响应结果：{}", url.getRemark(), body);
            logBuilder.result(body);

            if (body == null) {
                String errorMessage = "接口请求异常，未返回任何信息";
                logBuilder.statusCode(500)
                        .errorMessage(errorMessage);
                throw new SdportPayException(errorMessage);
            }
            if (isDirect) {
                return JsonUtils.parse(body, typeReference);
            }
            SdportPayCommonResult result = JsonUtils.parse(body, SdportPayCommonResult.class);
            if (!Boolean.TRUE.equals(result.getSuccess())) {
                logBuilder.statusCode(500)
                        .errorMessage(body);
                throw new SdportPayException(result.getMessage(), result);
            }

            return JsonUtils.parse(JsonUtils.toJsonString(result.getData()), typeReference);
        } catch (Exception e) {
            logBuilder.statusCode(500)
                    .errorMessage(ExceptionUtils.throwableToString(e, true));
            throw e;
        } finally {
            this.sysLogApi.saveLog(
                    logBuilder
                            .useTime(TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS))
                            .build()
            );
        }
    }

    /**
     * 创建请求URL
     *
     * @param url       URL
     * @param parameter 参数
     * @return 请求URL
     */
    public String createGetRequestUrl(SdportPayUrlEnum url, Object parameter) {
        Map<String, String> mapParameter = JsonUtils.parse(JsonUtils.toJsonString(parameter), new TypeReference<Map<String, String>>() {
        });
        String requestUrl = this.getRequestUrl(url);
        if (CollectionUtils.isEmpty(mapParameter)) {
            return requestUrl;
        }
        String parameterStr = mapParameter.entrySet().stream()
                .filter(item -> org.springframework.util.StringUtils.hasText(item.getValue()))
                .map(item -> item.getKey() + "=" + URLEncoder.encode(item.getValue(), StandardCharsets.UTF_8)).collect(Collectors.joining("&"));
        return requestUrl + "?" + parameterStr;
    }


    /**
     * 生成参数
     *
     * @param parameter 参数
     * @return 参数
     */
    private String createParameter(Object parameter) {
        HashMap<String, Object> mapParameter = parameter instanceof Map ? (HashMap<String, Object>) parameter : JsonUtils.parse(JsonUtils.toJsonString(parameter), new TypeReference<>() {
        });
        mapParameter.putAll(
                Map.of(
                        "signMethod", "01",
                        "timestamp", System.currentTimeMillis(),
                        "random", SmartIdGenerator.nextId() + "",
                        "eqSource", RequestSourceEnum.WEB.getCode()
                )
        );
        // 将参数转为map 并进行排序
        List<Map.Entry<String, Object>> entryList = new ArrayList<>(mapParameter.entrySet());

        LinkedHashMap<String, Object> sortMap = entryList.stream()
                .filter(item -> item.getValue() != null)
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        String content = JsonUtils.toJsonString(sortMap);
        String sign = this.createSign(content);
        sortMap.put("sign", sign);

        // 带着SIGN 一起排序
        LinkedHashMap<String, Object> sortParameterMap = sortMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        return JsonUtils.toJsonString(sortParameterMap);
    }


    /**
     * 生成签名
     *
     * @param content 需要签名的内容
     * @return 签名
     */
    @SneakyThrows({NoSuchAlgorithmException.class, InvalidKeyException.class, SignatureException.class})
    private String createSign(String content) {
        PrivateKey privateKey = RsaUtils.generaPrivateKey(sysParameterApi.getParameter(PRIVATE_KEY));

        Signature signature = Signature.getInstance(SIGNATURE);

        signature.initSign(privateKey);
        signature.update(content.getBytes(StandardCharsets.UTF_8));

        byte[] signByte = signature.sign();

        return Base64Utils.encode(signByte);
    }

    /**
     * 验证签名
     *
     * @param content 内容
     * @param sign    签名
     * @return 验证结果
     */
    @SneakyThrows({NoSuchAlgorithmException.class, InvalidKeyException.class, SignatureException.class})
    private boolean verifySign(String content, String sign) {
        PublicKey publicKey = RsaUtils.generaPublicKey(sysParameterApi.getParameter(PUBLIC_KEY));
        Signature signature = Signature.getInstance(SIGNATURE);

        signature.initVerify(publicKey);
        signature.update(content.getBytes(StandardCharsets.UTF_8));

        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * 获取请求地址
     *
     * @param url 请求地址
     * @return 请求地址
     */
    public String getRequestUrl(SdportPayUrlEnum url) {
        String baseUrl = sysParameterApi.getParameter(PAY_BASE_URL_PARAM);
        return baseUrl + url.getUrl();
    }

    /**
     * 获取通知地址
     *
     * @param notifyUrl 通知地址
     * @return 通知地址
     */
    protected String getNotifyUrl(NotifyUrlEnum notifyUrl) {
        String notifyBaseUrl = sysParameterApi.getParameter(NOTIFY_BASE_URL);
        return notifyBaseUrl + notifyUrl.getUrl();
    }
}
