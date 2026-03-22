package com.smart.module.sso.server.auth.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.module.sso.server.auth.SmartSsoServerAuthProperties;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientPO;
import com.smart.module.sso.server.common.manager.repository.SsoOauth2ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.time.chrono.ChronoZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据库注册客户端存储库实现类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/20 14:18
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class SmartDbRegisteredClientRepositoryImpl implements RegisteredClientRepository {

    private final SmartSsoServerAuthProperties ssoServerAuthProperties;
    private final SsoOauth2ClientRepository oauth2ClientRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RegisteredClient registeredClient) {
        SsoOauth2ClientPO oauth2Client = this.registeredClientToOauth2Client(registeredClient);
        this.oauth2ClientRepository.saveOrUpdate(oauth2Client);
    }


    @Override
    public RegisteredClient findById(String id) {
        SsoOauth2ClientPO oauth2Client = this.oauth2ClientRepository.getById(Long.valueOf(id));
        if (oauth2Client == null) {
            return null;
        }
        if (!Boolean.TRUE.equals(oauth2Client.getUseYn())) {
            return null;
        }
        return this.oauth2ClientToRegisteredClient(oauth2Client);
    }


    @Override
    public RegisteredClient findByClientId(String clientId) {
        SsoOauth2ClientPO oauth2Client = this.oauth2ClientRepository.lambdaQuery()
                .eq(SsoOauth2ClientPO::getClientCode, clientId)
                .one();
        if (oauth2Client == null) {
            return null;
        }
        if (!Boolean.TRUE.equals(oauth2Client.getUseYn())) {
            return null;
        }
        return this.oauth2ClientToRegisteredClient(oauth2Client);
    }

    /**
     * 将Oauth2ClientPO转换为RegisteredClient
     * @param oauth2Client SsoOauth2ClientPO
     * @return RegisteredClient
     */
    protected RegisteredClient oauth2ClientToRegisteredClient(SsoOauth2ClientPO oauth2Client) {
        if (oauth2Client == null) {
            return null;
        }

        // 客户端认证方法
        Set<ClientAuthenticationMethod> clientAuthenticationMethods = this.convertStrToList(oauth2Client.getClientAuthenticationMethods(), ClientAuthenticationMethod::valueOf);
        // 授权类型
        Set<AuthorizationGrantType> authorizationGrantTypes = this.convertStrToList(oauth2Client.getAuthorizationGrantTypes(), AuthorizationGrantType::new);
        Set<String> redirectUris = this.convertStrToList(oauth2Client.getRedirectUri(), item -> item);
        Set<String> postLogoutRedirectUris = this.convertStrToList(oauth2Client.getPostLogoutRedirectUri(), item -> item);
        Set<String> dbScopes = this.convertStrToList(oauth2Client.getScopes(), item -> item);

        RegisteredClient.Builder clientBuilder = RegisteredClient.withId(oauth2Client.getId().toString())
                .clientId(oauth2Client.getClientCode())
                .clientIdIssuedAt(Optional.ofNullable(oauth2Client.getCreateTime()).map(ChronoZonedDateTime::toInstant).orElse(null))
                .clientSecret(Objects.requireNonNullElseGet(oauth2Client.getClientSecret(), this.ssoServerAuthProperties::getDefaultSecret))
                .clientSecretExpiresAt(Optional.ofNullable(oauth2Client.getClientSecretExpire()).map(ChronoZonedDateTime::toInstant).orElse(null))
                .clientName(oauth2Client.getClientName())
                .clientAuthenticationMethods(methods -> methods.addAll(clientAuthenticationMethods))
                .authorizationGrantTypes(grantTypes -> grantTypes.addAll(authorizationGrantTypes))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
                .scopes(scopes -> scopes.addAll(dbScopes));
        if (StringUtils.hasText(oauth2Client.getClientSettings())) {
            Map<String, Object> clientSettings = JsonUtils.parse(oauth2Client.getClientSettings(), new TypeReference<>() {
            });
            clientBuilder.clientSettings(ClientSettings.withSettings(clientSettings).build());
        }
        if (StringUtils.hasText(oauth2Client.getTokenSettings())) {
            Map<String, Object> tokenSettings = JsonUtils.parse(oauth2Client.getTokenSettings(), new TypeReference<>() {
            });
            clientBuilder.tokenSettings(TokenSettings.withSettings(tokenSettings).build());
        }
        return clientBuilder.build();
    }

    /**
     * 将RegisteredClient转换为Oauth2ClientPO
     * @param registeredClient RegisteredClient
     * @return SsoOauth2ClientPO
     */
    protected SsoOauth2ClientPO registeredClientToOauth2Client(RegisteredClient registeredClient) {
        if (registeredClient == null) {
            return null;
        }
        SsoOauth2ClientPO oauth2Client = new SsoOauth2ClientPO();
        oauth2Client.setId(Long.valueOf(registeredClient.getId()));
        oauth2Client.setClientCode(registeredClient.getClientId());
        oauth2Client.setClientSecret(registeredClient.getClientSecret());
        oauth2Client.setClientSecretExpire(Optional.ofNullable(registeredClient.getClientSecretExpiresAt()).map(item -> item.atZone(ZoneId.systemDefault())).orElse(null));
        oauth2Client.setClientName(registeredClient.getClientName());
        oauth2Client.setClientAuthenticationMethods(new ArrayList<>(registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).toList()));
        oauth2Client.setAuthorizationGrantTypes(new ArrayList<>(registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).toList()));
        oauth2Client.setRedirectUri(new ArrayList<>(registeredClient.getRedirectUris()));
        oauth2Client.setPostLogoutRedirectUri(new ArrayList<>(registeredClient.getPostLogoutRedirectUris()));
        oauth2Client.setScopes(new ArrayList<>(registeredClient.getScopes()));
        oauth2Client.setClientSettings(JsonUtils.toJsonString(registeredClient.getClientSettings()));
        oauth2Client.setTokenSettings(JsonUtils.toJsonString(registeredClient.getTokenSettings()));
        return oauth2Client;
    }

    /**
     * 将字符串转换为列表
     * @param dataList 字符串列表
     * @param mapper 映射函数
     * @return 列表
     * @param <T> 列表元素类型
     */
    protected <T> Set<T> convertStrToList(List<String> dataList, Function<String, T> mapper) {
        if (!CollectionUtils.isEmpty(dataList)) {
            return Collections.emptySet();
        }
        return dataList.stream()
                .filter(StringUtils::hasText)
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
