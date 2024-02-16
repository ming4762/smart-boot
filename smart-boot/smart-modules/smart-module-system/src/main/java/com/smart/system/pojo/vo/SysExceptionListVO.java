package com.smart.system.pojo.vo;

import com.smart.system.model.SysExceptionPO;
import com.smart.system.model.SysUserPO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2024/2/16 15:09
 * @since 3.0.0
 */
@Getter
@Setter
public class SysExceptionListVO extends SysExceptionPO {
    @Serial
    private static final long serialVersionUID = 7297923753641156750L;

    private SysUserPO resolvedUser;
}
