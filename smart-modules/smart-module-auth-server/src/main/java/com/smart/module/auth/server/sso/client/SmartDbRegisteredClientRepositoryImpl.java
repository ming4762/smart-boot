package com.smart.module.auth.server.sso.client;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.module.auth.server.manager.model.Oauth2ClientPO;
import com.smart.module.auth.server.manager.service.Oauth2ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
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

    private static final String STR_DELIMITER = "\n";

    private final Oauth2ClientService oauth2ClientService;

    @Override
    public void save(RegisteredClient registeredClient) {
        Oauth2ClientPO oauth2Client = this.registeredClientToOauth2Client(registeredClient);
        this.oauth2ClientService.saveOrUpdate(oauth2Client);
    }


    @Override
    public RegisteredClient findById(String id) {
        Oauth2ClientPO oauth2Client = this.oauth2ClientService.getByIdInUse(Long.valueOf(id));
        return this.oauth2ClientToRegisteredClient(oauth2Client);
    }


    @Override
    public RegisteredClient findByClientId(String clientId) {
        Oauth2ClientPO oauth2Client = this.oauth2ClientService.getByClientId(clientId);
        return this.oauth2ClientToRegisteredClient(oauth2Client);
    }

    /**
     * 将Oauth2ClientPO转换为RegisteredClient
     * @param oauth2Client Oauth2ClientPO
     * @return RegisteredClient
     */
    protected RegisteredClient oauth2ClientToRegisteredClient(Oauth2ClientPO oauth2Client) {
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
                .clientId(oauth2Client.getClientId())
                .clientIdIssuedAt(Optional.ofNullable(oauth2Client.getCreateTime()).map(ChronoZonedDateTime::toInstant).orElse(null))
                .clientSecret(oauth2Client.getClientSecret())
                .clientSecretExpiresAt(Optional.ofNullable(oauth2Client.getClientSecretExpire()).map(ChronoZonedDateTime::toInstant).orElse(null))
                .clientName(oauth2Client.getClientName())
                .clientAuthenticationMethods(methods -> methods.addAll(clientAuthenticationMethods))
                .authorizationGrantTypes(grantTypes -> grantTypes.addAll(authorizationGrantTypes))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
                .scopes(scopes -> scopes.addAll(dbScopes));
        if (StringUtils.hasText(oauth2Client.getClientSettings())) {
            clientBuilder.clientSettings(JsonUtils.parse(oauth2Client.getClientSettings(), ClientSettings.class));
        }
        if (StringUtils.hasText(oauth2Client.getTokenSettings())) {
            clientBuilder.tokenSettings(JsonUtils.parse(oauth2Client.getTokenSettings(), TokenSettings.class));
        }
        return clientBuilder.build();
    }

    /**
     * 将RegisteredClient转换为Oauth2ClientPO
     * @param registeredClient RegisteredClient
     * @return Oauth2ClientPO
     */
    protected Oauth2ClientPO registeredClientToOauth2Client(RegisteredClient registeredClient) {
        if (registeredClient == null) {
            return null;
        }
        Oauth2ClientPO oauth2Client = new Oauth2ClientPO();
        oauth2Client.setId(Long.valueOf(registeredClient.getId()));
        oauth2Client.setClientId(registeredClient.getClientId());
        oauth2Client.setClientSecret(registeredClient.getClientSecret());
        oauth2Client.setClientSecretExpire(Optional.ofNullable(registeredClient.getClientSecretExpiresAt()).map(item -> item.atZone(ZoneId.systemDefault())).orElse(null));
        oauth2Client.setClientName(registeredClient.getClientName());
        oauth2Client.setClientAuthenticationMethods(registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.joining(STR_DELIMITER)));
        oauth2Client.setAuthorizationGrantTypes(registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.joining(STR_DELIMITER)));
        oauth2Client.setRedirectUri(String.join(STR_DELIMITER, registeredClient.getRedirectUris()));
        oauth2Client.setPostLogoutRedirectUri(String.join(STR_DELIMITER, registeredClient.getPostLogoutRedirectUris()));
        oauth2Client.setScopes(String.join(STR_DELIMITER, registeredClient.getScopes()));
        oauth2Client.setClientSettings(JsonUtils.toJsonString(registeredClient.getClientSettings()));
        oauth2Client.setTokenSettings(JsonUtils.toJsonString(registeredClient.getTokenSettings()));
        return oauth2Client;
    }

    /**
     * 将字符串转换为列表
     * @param str 字符串
     * @param mapper 映射函数
     * @return 列表
     * @param <T> 列表元素类型
     */
    protected <T> Set<T> convertStrToList(String str, Function<String, T> mapper) {
        if (!StringUtils.hasText(str)) {
            return Collections.emptySet();
        }
        return Arrays.stream(str.split(STR_DELIMITER))
                .filter(StringUtils::hasText)
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
