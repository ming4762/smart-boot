package com.smart.framework.commons.core.http;

import java.io.Serializable;

/**
 * http 代码
 * @author jackson
 * 2020/2/15 7:17 下午
 */
public interface IHttpStatus extends Serializable {

    /**
     * 获取状态码
     * @return 状态码
     */
    Integer getCode();

    /**
     * 子编码
     * @return 子编码
     */
    default Integer getSubCode() {
        return null;
    }

    /**
     * 获取状态信息
     * @return 状态信息
     */
    String getMessage();
}
