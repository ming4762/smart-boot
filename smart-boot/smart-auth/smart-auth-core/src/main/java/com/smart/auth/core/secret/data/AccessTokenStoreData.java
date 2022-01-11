package com.smart.auth.core.secret.data;

import com.smart.auth.core.userdetails.RestUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

/**
 * @author ShiZhongMing
 * @since 1.0.7
 */
@Getter
@Setter
@AllArgsConstructor
public class AccessTokenStoreData {

    private RestUserDetails app;

    private List<AccessTokenStoreDataItem> accessTokenList;

    @Getter
    @AllArgsConstructor
    public static class AccessTokenStoreDataItem {
        private final String accessToken;

        private final Instant createTime;
    }
}
