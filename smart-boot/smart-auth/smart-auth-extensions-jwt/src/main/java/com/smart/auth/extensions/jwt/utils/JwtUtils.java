package com.smart.auth.extensions.jwt.utils;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.model.*;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.http.HttpMethod;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;

import java.security.Key;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * jwt工具类
 * @author jackson
 * 2020/2/14 10:34 下午
 */
@Slf4j
public class JwtUtils {

    private JwtUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String USER_KEY = "user";

    private static final String ROLE_KEY = "role";

    private static final String PERMISSION_KEY = "permission";

    static {
        ConvertUtils.register(new DateConverter(null), Date.class);
        ConvertUtils.register(new Converter() {
            @Override
            public <T> T convert(Class<T> type, Object value) {
                if (value == null) {
                    return null;
                }
                return (T) AuthTypeEnum.valueOf(value.toString());
            }
        }, AuthTypeEnum.class);
        ConvertUtils.register(new Converter() {
            @Override
            public <T> T convert(Class<T> type, Object value) {
                if (value == null) {
                    return null;
                }
                return (T) LoginTypeEnum.valueOf(value.toString());
            }
        }, LoginTypeEnum.class);
    }

    /**
     * 创建jwt
     * @param userDetails 用户信息
     * @return jwt
     */
    public static String createJwt(RestUserDetails userDetails, Key key) {
        final Date now = new Date();
        final JwtBuilder builder = Jwts.builder()
                .setId(userDetails.getUserId().toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .claim(ROLE_KEY, userDetails.getRoles())
                .claim(PERMISSION_KEY, userDetails.getPermissions())
                .claim(USER_KEY, userDetails);
        return builder.compact();
    }


    /**
     * 解析JWT
     *
     * @param jwt JWT
     * @return {@link Claims}
     */
    public static Claims parseJwt(String jwt, Key key) {
        var jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        return jwtParser.parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 获取用户名
     * @param jwt JWT
     * @param key key
     * @return 用户名
     */
    public static String getUsername(String jwt, Key key) {
        return parseJwt(jwt, key).getSubject();
    }

    /**
     * 获取用户信息
     * @param jwt jwt
     * @param key key
     * @return 用户信息
     */
    @SuppressWarnings("unchecked")
    public static RestUserDetailsImpl getUser(String jwt, Key key) {
        try {
            final Claims claims = parseJwt(jwt, key);
            final RestUserDetailsImpl restUserDetails = new RestUserDetailsImpl();
            BeanUtils.populate(restUserDetails, claims.get(USER_KEY, Map.class));
            restUserDetails.setLoginTime(claims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            // 设置token
            restUserDetails.setToken(jwt);
            // 设置权限
            final List<Map<String, String>> permissionMap = claims.get(PERMISSION_KEY, List.class);
            final List<Permission> permissions = permissionMap.stream()
                    .map(item -> {
                        Permission permission = new Permission();
                        permission.setAuthority(item.get("authority"));
                        permission.setUrl(item.get("url"));
                        String method = item.get("method");
                        if (StringUtils.isNotBlank(method)) {
                            permission.setMethod(HttpMethod.resolve(method));
                        }
                        return permission;
                    }).collect(Collectors.toList());
            // 设置角色
            final List<String> roles = claims.get(ROLE_KEY, List.class);
            final Set<SmartGrantedAuthority> authorities = new HashSet<>(permissions.size() + roles.size());
            permissions.forEach(permission -> authorities.add(new PermissionGrantedAuthority(permission)));
            roles.forEach(item -> authorities.add(new RoleGrantedAuthority(item)));
            restUserDetails.setAuthorities(authorities);
            return restUserDetails;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadCredentialsException( "JWT解析失败", e);
        }
    }

    /**
     * 获取jwt
     * @param request 请求体
     * @return JWT
     */
    @Nullable
    public static String getJwt(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
