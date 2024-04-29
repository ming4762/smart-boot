package com.smart.dingtalk.pojo.dto;

import lombok.*;

import java.io.Serial;

/**
 * 根据手机号查询用户 结果
 * @author shizhongming
 * 2024/4/28 15:21
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByMobileResult extends AbstractDingtalkResult {
    @Serial
    private static final long serialVersionUID = -2516419093546536505L;

    /**
     * 钉钉用户ID
     */
    private String userId;
}
