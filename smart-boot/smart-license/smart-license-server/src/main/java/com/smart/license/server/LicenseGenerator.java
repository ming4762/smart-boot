package com.smart.license.server;

/**
 * License生成器
 * @author zhongming4762
 * 2022/12/18 15:35
 */
public interface LicenseGenerator {

    /**
     * 生成License
     * @param parameter license生成参数
     * @return license
     */
    boolean generate(LicenseGeneratorParameter parameter);

}
