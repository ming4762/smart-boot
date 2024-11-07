package com.smart.framework.auth.core.properties;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 忽略的请求设置
 * @author shizhongming
 * 2024/11/6 13:44
 * @since 5.0.0
 */
@Getter
@Setter
public class AuthIgnoreProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 2549392442912199079L;

    /**
     * 需要忽略的 URL 格式，不考虑请求方法
     */
    private List<String> pattern = Lists.newArrayList("/public/**");

    public List<String> getPattern() {
        String publicStr = "/public/**";
        if (!pattern.contains(publicStr)) {
            pattern.add(publicStr);
        }
        return pattern;
    }

    /**
     * 需要忽略的 GET 请求
     */
    private List<String> get = Lists.newArrayList();

    /**
     * 需要忽略的 POST 请求
     */
    private List<String> post = Lists.newArrayList();

    /**
     * 需要忽略的 DELETE 请求
     */
    private List<String> delete = Lists.newArrayList();

    /**
     * 需要忽略的 PUT 请求
     */
    private List<String> put = Lists.newArrayList();

    /**
     * 需要忽略的 HEAD 请求
     */
    private List<String> head = Lists.newArrayList();

    /**
     * 需要忽略的 PATCH 请求
     */
    private List<String> patch = Lists.newArrayList();

    /**
     * 需要忽略的 OPTIONS 请求
     */
    private List<String> options = Lists.newArrayList();

    /**
     * 需要忽略的 TRACE 请求
     */
    private List<String> trace = Lists.newArrayList();
}
