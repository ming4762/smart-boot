package com.smart.monitor.server.core.event;

import java.io.Serializable;

/**
 * 事件编码接口
 * @author shizhongming
 * 2021/3/27 7:12 下午
 */
public interface EventCode extends Serializable {

    /**
     * 获取事件编码
     * @return 事件编码
     */
    String getCode();

}
