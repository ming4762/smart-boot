package com.smart.auth.core.model;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2021/6/4 11:26
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsLoginParameter {

    private String phone;

    private String code;

    public static SmsLoginParameter create(@NonNull HttpServletRequest request) {
        return SmsLoginParameter.builder()
                .phone(request.getParameter("phone"))
                .code(request.getParameter("code"))
                .build();
    }
}
