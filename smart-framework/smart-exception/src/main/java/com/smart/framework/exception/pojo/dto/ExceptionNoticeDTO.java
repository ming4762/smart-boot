package com.smart.framework.exception.pojo.dto;

import lombok.*;
import org.jspecify.annotations.NonNull;

/**
 * 异常通知DTO
 * @author shizhongming
 * 2025/8/28 17:09
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ExceptionNoticeDTO {

    @NonNull
    private Exception exception;

    @NonNull
    private Long exceptionNo;

    @NonNull
    private String requestIp;

    @NonNull
    private String requestPath;
}
