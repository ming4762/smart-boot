package com.smart.auth.security.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 临时令牌申请DTO
 * @author ShiZhongMing
 * 2021/6/23 8:30
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class TempTokenApplyDTO {

    public TempTokenApplyDTO() {
        this.once = Boolean.TRUE;
    }

    /**
     * 申请的资源
     */
    @NotNull(message = "资源不能为空")
    private String resource;

    /**
     * 是否只使用一次
     */
    private Boolean once;
}
