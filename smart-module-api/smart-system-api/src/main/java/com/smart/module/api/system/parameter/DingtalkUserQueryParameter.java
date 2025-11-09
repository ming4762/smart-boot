package com.smart.module.api.system.parameter;

import lombok.*;

import java.io.Serializable;

/**
 * 钉钉用户查询参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/5 15:35
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DingtalkUserQueryParameter implements Serializable {

    /**
     * 钉钉用户的unionId
     */
    private String unionId;

     /**
      * 钉钉用户的openId
      */
    private String openId;

     /**
      * 钉钉用户的手机号
      */
    private String mobile;

     /**
      * 钉钉应用的clientId
      */
    private String clientId;
}
