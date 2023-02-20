package com.smart.system.pojo.vo.license;

import com.smart.system.model.auth.SmartAuthLicensePO;
import com.smart.system.model.SysSystemPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhongming4762
 * 2023/2/8 20:23
 */
@Getter
@Setter
@ToString
public class SmartAuthLicenseQueryVO extends SmartAuthLicensePO {

    private SysSystemPO system;
}
