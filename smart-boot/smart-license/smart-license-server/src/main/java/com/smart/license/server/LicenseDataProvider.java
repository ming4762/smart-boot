package com.smart.license.server;

import java.util.HashMap;
import java.util.Map;

/**
 * license数据提供器
 * @author zhongming4762
 * 2022/12/27 21:28
 */
public interface LicenseDataProvider {

    /**
     * 数据的key
     * @return key
     */
    String key();

    /**
     * 数据
     * @param parameter license生成参数
     * @return 数据
     */
    default Object data(LicenseGeneratorParameter parameter) {
        return null;
    }

    /**
     * 获取返回的数据
     * @param parameter license生成参数
     * @return 数据
     */
    default Map<String, Object> dataMap(LicenseGeneratorParameter parameter) {
        Map<String, Object> dataMap = new HashMap<>(1);
        dataMap.put(this.key(), this.data(parameter));
        return dataMap;
    }
}
