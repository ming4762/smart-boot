package com.smart.module.system.pojo.dto.timezone;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 设置时区DTO
 * @author shizhongming
 * 2025/10/30 15:43
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class SysSetTimezoneDTO {
    @NotBlank(message = "时区不能为空")
    private String timezone;
}
