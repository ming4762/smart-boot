package com.smart.auth.core.token;

import com.smart.auth.core.userdetails.RestUserDetails;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

/**
 * token存储器
 * @author zhongming4762
 * 2023/3/6
 */
public interface TokenRepository extends Ordered {

    /**
     * 保存JWT
     * @param user 用户信息
     * @return 是否保存成功
     */
    String save(@NonNull RestUserDetails user);

    /**
     * 验证JWT
     * @param token token
     * @param user 用户信息
     * @return 验证结果
     */
    boolean validate(@NonNull String token, @NonNull RestUserDetails user);

    /**
     * 通过token失效
     * @param token token
     * @param username 用户名
     * @return 是否成功
     */
    boolean invalidateByToken(@NonNull String username, @NonNull String token);

    /**
     * 使用户登录失效
     * @param username 用户名
     * @return 是否成功
     */
    boolean invalidateByUsername(@NonNull String username);

    /**
     * 查询所有JWT
     * @return 所有jwt
     */
    @NonNull
    Set<String> listAll();

    /**
     * 通过用户名查询JWT
     * @param username 用户名
     * @return jwt列表
     */
    @NonNull
    Set<String> listAll(@NonNull String username);

    /**
     * 查询所有数据
     * @return jwt数据
     */
    @NonNull
    List<TokenData> listData();

    /**
     * 通过用户名查询jwt数据
     * @param username 用户名
     * @return jwt数据
     */
    @NonNull
    List<TokenData> listData(@NonNull String username);
}
