package com.smart.framework.auth.extensions.jwt.service;

import com.smart.framework.auth.core.model.PermissionGrantedAuthority;
import com.smart.framework.auth.core.model.RestUserDetailsImpl;
import com.smart.framework.auth.core.model.RoleGrantedAuthority;
import com.smart.framework.auth.core.model.SmartGrantedAuthority;
import com.smart.framework.auth.core.userdetails.RestUserDetails;
import com.smart.framework.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.framework.commons.core.dto.auth.AuthRole;
import com.smart.framework.commons.core.dto.auth.Permission;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.jwt.Jwt;
import com.smart.framework.commons.jwt.JwtDecoder;
import com.smart.framework.commons.jwt.JwtEncoder;
import com.smart.framework.commons.jwt.JwtEncoderParameters;
import com.smart.framework.commons.jwt.algorithm.SignatureAlgorithm;
import com.smart.framework.commons.jwt.claim.JwtClaimsSet;
import com.smart.framework.commons.jwt.header.JwsHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * JWT服务层
 * 1、登出逻辑
 * 2、jwt解析接口
 * 3、jwtStore接口 cache实现
 * @author ShiZhongMing
 * 2020/12/31 15:43
 * @since 1.0
 */
@Slf4j
public class JwtService implements JwtResolver {

    private static final String USER_KEY = "user";

    private static final String ROLE_KEY = "role";

    private static final String PERMISSION_KEY = "permission";

    private JwtDecoder jwtDecoder;

    private JwtEncoder jwtEncoder;

    private final boolean permissionCache;

    public JwtService(Boolean permissionCache) {
        this.permissionCache = Boolean.TRUE.equals(permissionCache);
    }

    @Override
    public RestUserDetails resolver(@NonNull String jwtStr) {
        Jwt jwt = this.jwtDecoder.decode(jwtStr);
        Map<String, Object> claims = jwt.getClaims();
        RestUserDetailsImpl userDetails = JsonUtils.parse((String) claims.get(USER_KEY), RestUserDetailsImpl.class);

        if (!this.permissionCache) {
            List<AuthRole> roleStrList = JsonUtils.parseCollection((String) claims.get(ROLE_KEY), AuthRole.class);
            List<Permission> permissionList = JsonUtils.parseCollection((String) claims.get(PERMISSION_KEY), Permission.class);
            Set<SmartGrantedAuthority> authorities = new HashSet<>(permissionList.size() + roleStrList.size());
            // 添加权限信息
            permissionList.forEach(permission -> authorities.add(new PermissionGrantedAuthority(permission)));
            // 添加角色信息
            roleStrList.forEach(item -> authorities.add(new RoleGrantedAuthority(item)));

            userDetails.setAuthorities(authorities);
        } else {
            // TODO: do nothing
        }
        // 设置token
        userDetails.setToken(jwtStr);
        return userDetails;
    }

    @Override
    public String create(RestUserDetails userDetails) {
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .id(userDetails.getUserId().toString())
                .claim(USER_KEY, JsonUtils.toJsonString(userDetails));
        if (!this.permissionCache) {
            Set<AuthRole> roles = userDetails.getRoles();
            Set<Permission> permissions = userDetails.getPermissions();
            builder.claim(ROLE_KEY, JsonUtils.toJsonString(roles))
                    .claim(PERMISSION_KEY, JsonUtils.toJsonString(permissions));
        }
        JwtEncoderParameters parameters = JwtEncoderParameters.builder()
                .jwsHeader(JwsHeader.builder().algorithm(SignatureAlgorithm.RS256).build())
                .claims(builder.build())
                .build();
        return this.jwtEncoder.encode(parameters).getTokenValue();
    }



    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Autowired
    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Autowired
    public void setJwtEncoder(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
}
