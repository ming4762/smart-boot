package com.smart.auth.extensions.appsecret.handler;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.data.AppDetails;
import com.smart.auth.core.secret.store.AccessTokenStore;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.appsecret.service.AccessTokenCreator;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.RestJsonWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * @author ShiZhongMing
 * @since 1.0.7
 */
public class AppLoginSuccessHandler implements AuthenticationSuccessHandler {

    private AccessTokenCreator accessTokenCreator;

    private AccessTokenStore accessTokenStore;

    private AuthProperties authProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        RestUserDetails userDetails = (RestUserDetails) authentication.getDetails();
        AppDetails appDetails = new AppDetails(userDetails.getUsername(), userDetails.getPassword(), userDetails.getIpWhiteList());
        String accessToken = this.accessTokenCreator.createAccessToken(appDetails);
        this.accessTokenStore.save(userDetails, accessToken);
        RestJsonWriter.writeJson(response, Result.success(new AppLoginSuccessVO(accessToken, this.authProperties.getAppsecret().getAccessTokenTimeout().getSeconds())));
    }

    @Autowired
    public void setAccessTokenCreator(AccessTokenCreator accessTokenCreator) {
        this.accessTokenCreator = accessTokenCreator;
    }

    @Autowired
    public void setAuthProperties(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Autowired
    public void setAccessTokenStore(AccessTokenStore accessTokenStore) {
        this.accessTokenStore = accessTokenStore;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class AppLoginSuccessVO {
        private final String accessToken;

        private final Long expiresIn;
    }
}
