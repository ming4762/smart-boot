package com.smart.module.system.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.framework.auth.common.constants.AccessSignatureEnum;
import com.smart.framework.commons.core.dto.auth.AuthAkSkCreateTokenDTO;
import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.utils.Base64Utils;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.core.utils.RestUtils;
import com.smart.framework.commons.core.utils.SmartIdGenerator;
import com.smart.framework.commons.core.utils.auth.SecretUtils;
import com.smart.framework.commons.core.utils.auth.ShaUtils;
import com.smart.framework.crud.constants.CrudCommonEnum;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.mapper.auth.SysAuthAccessSecretMapper;
import com.smart.module.system.model.auth.SysAuthAccessSecretPO;
import com.smart.module.system.pojo.dto.auth.SmartAuthAccessTestDTO;
import com.smart.module.system.pojo.vo.SysAuthAccessSecretListVO;
import com.smart.module.system.service.auth.SysAuthAccessSecretService;
import com.smart.module.system.service.tenant.SysTenantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* sys_auth_access_secret -  Service实现类
* @author SmartCodeGenerator
* 2023年10月26日 下午7:25:26
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class SysAuthAccessSecretServiceImpl extends BaseServiceImpl<SysAuthAccessSecretMapper, SysAuthAccessSecretPO> implements SysAuthAccessSecretService {

    private final SysTenantService sysTenantService;

    /**
     * 插入一条记录（选择字段，策略插入）
     * 重写保存函数，自动生成ACCESS_KEY和 SECRET_KEY
     * @param entity 实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysAuthAccessSecretPO entity) {
        entity.setAccessKey(Base64Utils.encode(ShaUtils.hmacSha256Encrypt(UUID.randomUUID().toString(), SmartIdGenerator.nextId() + "")));
        entity.setSecretKey(Base64Utils.encode(ShaUtils.hmacSha256Encrypt(UUID.randomUUID().toString(), SmartIdGenerator.nextId() + "")));
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(SysAuthAccessSecretPO entity) {
        if (this.isAdd(entity)) {
            return this.save(entity);
        }
        return this.updateById(entity);
    }

    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<SysAuthAccessSecretPO> list(@NonNull QueryWrapper<SysAuthAccessSecretPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<SysAuthAccessSecretPO> list = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<SysAuthAccessSecretListVO> voList = list.stream()
                .map(item -> {
                    SysAuthAccessSecretListVO vo = new SysAuthAccessSecretListVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).toList();
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.WITH_ALL))) {
            this.sysTenantService.injectTenant(voList);
        }
        return voList.stream().map(SysAuthAccessSecretPO.class::cast).toList();
    }

    /**
     * 测试访问权限
     *
     * @param parameter 测试参数
     * @return 是否通过
     */
    @Override
    public String testAccessSecret(HttpServletRequest request, SmartAuthAccessTestDTO parameter) {
        SysAuthAccessSecretPO accessSecret = this.getById(parameter.getAccessId());
        if (accessSecret == null) {
            throw new SystemException("查询Access secret失败");
        }
        String httpMethod = request.getMethod().toUpperCase();
        String data = request.getHeader(HttpHeaders.DATE);
        if (data == null) {
            data = SecretUtils.getSignDate(ZonedDateTime.now());
        }
        String parameterStr = Stream.of("/access/api/test/test", parameter.getQueryParameter(), parameter.getJsonParameter() == null ? null : JsonUtils.toJsonString(JsonUtils.parse(parameter.getJsonParameter())))
                .filter(StringUtils::hasText)
                .collect(Collectors.joining());
        String sign = SecretUtils.createSign(
                AuthAkSkCreateTokenDTO.builder()
                        .httpMethod(HttpMethod.valueOf(httpMethod))
                        .contentType(parameter.getContentType())
                        .nonce(parameter.getNonce())
                        .parameterStr(parameterStr)
                        .prefix(parameter.getTokenPrefix())
                        .accessKey(accessSecret.getAccessKey())
                        .secretKey(accessSecret.getSecretKey())
                        .build(),
                data
        );
        String url = Stream.of(
                        this.getBaseUrl(request) + "/access/api/test/test?Authorization=" + URLEncoder.encode(sign, StandardCharsets.UTF_8),
                        parameter.getQueryParameter(),
                        String.format("%s=%s", AccessSignatureEnum.X_SIGNATURE_NONCE.getKey(), parameter.getNonce())
                ).filter(StringUtils::hasText)
                .collect(Collectors.joining("&"));
        Map<String, String> headers = new HashMap<>(Map.of(HttpHeaders.DATE, data));
        if (StringUtils.hasText(parameter.getContentType())) {
            headers.put(HttpHeaders.CONTENT_TYPE, parameter.getContentType());
        }
        String resultStr = RestUtils.rest(
                url,
                HttpMethod.POST,
                headers,
                parameter.getJsonParameter(),
                new ParameterizedTypeReference<>() {
                },
                null
        );
        Result<Map<String, Object>> result = JsonUtils.parse(resultStr, new TypeReference<>() {
        });
        log.info("测试访问权限,结果:{}", JsonUtils.toJsonString(resultStr));
        if (!result.isSuccess()) {
            throw new BusinessException(result.getMessage());
        }
        return sign;
    }

    /**
     * 获取baseUrl
     * @param request 请求
     * @return baseUrl
     */
    public String getBaseUrl(HttpServletRequest request) {
        // 获取协议 http 或 https
        // 注意：这只是偏好，不作为必须条件。
        String scheme = request.getScheme();
        // 获取主机名
        String serverName = request.getServerName();
        // 获取端口
        int serverPort = request.getServerPort();

        // 拼接成完整的 base URL
        return scheme + "://" + serverName + (serverPort == 80 || serverPort == 443 ? "" : ":" + serverPort);
    }
}