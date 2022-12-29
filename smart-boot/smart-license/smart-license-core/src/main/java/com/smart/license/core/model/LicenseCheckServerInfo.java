package com.smart.license.core.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 校验license参数
 * @author shizhongming
 * 2022/12/4 21:22
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LicenseCheckInfo implements Serializable {

    /**
     * mac地址列表
     */
    private List<String> macAddressList;

    /**
     * 允许访问的IP地址
     */
    private List<String> ipAddressList;

    /**
     * cpu序列号
     */
    private String cpuSerial;

    /**
     * 主板序列号
     */
    private String mainBoardSerial;
}
