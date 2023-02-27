package com.smart.system.constants;

import lombok.Getter;

/**
 * 系统参数code
 * @author zhongming4762
 * 2023/2/27
 */
public enum SysParameterCodeEnum {

    /**
     * 机密文件类型
     */
    SECRECY_FILE_TYPE("sys.fileType.secrecy")
    ;
    @Getter
    private final String code;

    SysParameterCodeEnum(String code) {
        this.code = code;
    }
}
