package com.smart.smc.inter.qingdaoport.api;

import cn.hutool.core.lang.func.LambdaUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.log.LogSourceEnum;
import com.smart.framework.commons.core.utils.BeanUtils;
import com.smart.framework.commons.core.utils.ExceptionUtils;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.core.utils.RestUtils;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import com.smart.smc.inter.qingdaoport.SmartSmcQingdaoPortProperties;
import com.smart.smc.inter.qingdaoport.auth.QingdaoPortSignParameter;
import com.smart.smc.inter.qingdaoport.auth.SignUtils;
import com.smart.smc.inter.qingdaoport.constants.QingdaoPortUrlEnum;
import com.smart.smc.inter.qingdaoport.dto.parameter.QingdaoPortRequestParameter;
import com.smart.smc.inter.qingdaoport.dto.result.QingdaoPortCommonResult;
import com.smart.smc.inter.qingdaoport.exception.QingdaoPortExcetion;
import com.smart.smc.inter.qingdaoport.support.QingdaoPortCustomHolder;
import com.smart.smc.inter.qingdaoport.support.QingdaoPortCustomerData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.lang.NonNull;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-05 15:26
 * @since 5.0.0
 */
@RequiredArgsConstructor
@Slf4j
public abstract class QingdaoPortCommonApi {

    private static final String PLATFORM = "SMC_QUEINGDAOPORT";
    private static final String LOG_IDENT = "30";

    private final SmartSmcQingdaoPortProperties properties;
    private final SysLogApi sysLogApi;


    /**
     * 执行云港通请求
     * @param url 请求地址枚举
     * @param parameter 请求参数
     * @param type 请求类型
     * @param saveResult 是否保存响应结果
     * @param typeReference 响应结果类型引用
     * @return 响应结果
     * @param <T> 响应结果类型
     */
    protected <T extends QingdaoPortCommonResult> T doRequest(QingdaoPortUrlEnum url, Object parameter, String type, boolean saveResult, TypeReference<T> typeReference) {
        // 1、创建查询参数
        QingdaoPortRequestParameter requestParameter = this.createRequestParameter(parameter, type);
        MultiValueMap<String, HttpEntity<?>> formParameter = this.createFormParameter(requestParameter);
        String parameterJson = JsonUtils.toJsonString(requestParameter);
        String requestUrl = this.getRequestUrl(url);
        log.info("发送云港通请求：{}，请求URL：{}，参数：{}", url.getRemark(), requestUrl, parameterJson);
        // 开始记录日志
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
                    this.getCommonHeaderMap(),
                    formParameter,
                    new ParameterizedTypeReference<>() {
                    },
                    null
                    );
            log.info("{}，响应结果：{}", url.getRemark(), body);
            if (saveResult) {
                logBuilder.result(body);
            }
            if (body == null) {
                String errorMessage = "接口请求异常，未返回任何信息";
                logBuilder.statusCode(500)
                        .errorMessage(errorMessage);
                throw new QingdaoPortExcetion(errorMessage);
            }
            T result = JsonUtils.parse(body, typeReference);
            if (!Boolean.TRUE.equals(result.getSuccess())) {
                logBuilder.statusCode(500)
                        .errorMessage(result.getMessage());
                throw new QingdaoPortExcetion(result.getMessage());
            }
            return result;
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
     * 创建云港通请求参数
     * 主要生成签名
     * @param parameter 请求参数
     * @param type 请求类型
     * @return 云港通请求参数
     */
    protected QingdaoPortRequestParameter createRequestParameter(Object parameter, String type) {
        if (parameter == null) {
            parameter = Collections.emptyMap();
        }
        QingdaoPortCustomerData customerData = this.getCustomerData();
        String customerCode = customerData.customerCode();
        String privateKey = customerData.privateKey();

        // 生成签名
        QingdaoPortSignParameter signParameter = new QingdaoPortSignParameter(customerCode, type, JsonUtils.toJsonString(parameter));
        Map<String, Object> signParameterMap = BeanUtils.beanToMap(signParameter);
        // 将签名参数中的null值过滤掉
        Map<String, Object> nonNullSignParameterMap = signParameterMap.entrySet().stream()
                .filter(item -> item.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String formatUrlMap = SignUtils.formatUrlMap(nonNullSignParameterMap, false, false);
        String sign = SignUtils.sign(formatUrlMap, privateKey, StandardCharsets.UTF_8);

        return new QingdaoPortRequestParameter(customerCode, sign, type, parameter);
    }

    /**
     * 创建云港通请求参数的表单参数
     * @param requestParameter 云港通请求参数
     * @return 表单参数
     */
    protected MultiValueMap<String, HttpEntity<?>> createFormParameter(QingdaoPortRequestParameter requestParameter) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part(LambdaUtil.getFieldName(QingdaoPortRequestParameter::getCustomerCode), requestParameter.getCustomerCode());
        builder.part(LambdaUtil.getFieldName(QingdaoPortRequestParameter::getSign), requestParameter.getSign());
        if (org.springframework.util.StringUtils.hasText(requestParameter.getType())) {
            builder.part(LambdaUtil.getFieldName(QingdaoPortRequestParameter::getType), requestParameter.getType());
        }
        builder.part(LambdaUtil.getFieldName(QingdaoPortRequestParameter::getData), JsonUtils.toJsonString(requestParameter.getData()));
        return builder.build();
    }

    /**
     * 获取云港通请求URL
     * @param url 请求地址枚举类
     * @return 请求URL
     */
    protected String getRequestUrl(QingdaoPortUrlEnum url) {
        return UriComponentsBuilder.fromPath(this.properties.getBaseUrl())
                .path(url.getUrl())
                .toUriString();
    }

    /**
     * 获取云港通通用请求头
     * @return 云港通通用请求头
     */
    protected Map<String, String> getCommonHeaderMap() {
        QingdaoPortCustomerData customerData = this.getCustomerData();
        return Map.of(
                "account", URLEncoder.encode(AuthUtils.getCurrentUsername(), StandardCharsets.UTF_8),
                "sysCode", customerData.sysCode(),
                HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE
        );
    }

    /**
     * 获取云港通客户数据
     * @return 云港通客户数据
     */
    @NonNull
    protected QingdaoPortCustomerData getCustomerData() {
        QingdaoPortCustomerData customerData = QingdaoPortCustomHolder.get();
        if (customerData == null) {
            throw new QingdaoPortExcetion("未配置客户信息");
        }
        return customerData;
    }
}
