package com.smart.system.pojo.dto.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统菜单访问记录保存参数
 * @author ShiZhongMing
 * 2022/7/8
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysMenuAccessLogSaveDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8443354901251492910L;

    private Long functionId;
}
