package com.smart.module.api.system.parameter;

import com.smart.module.api.system.constants.SysThirdPlatformSubTypeEnum;
import com.smart.module.api.system.constants.SysThirdPlatformTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 系统用户第三方账号参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-03 16:56
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUserThirdAccountParameter implements Serializable {

    @NotEmpty(message = "用户ID列表不能为空")
    private List<Long> userIdList;

    private SysThirdPlatformTypeEnum platformType;

    private SysThirdPlatformSubTypeEnum platformSubType;

    private String appid;
}
