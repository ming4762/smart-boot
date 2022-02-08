package com.smart.monitor.client.core.application;


import com.smart.monitor.core.model.Application;

/**
 * Application工厂
 * @author shizhongming
 * 2021/3/21
 */
public interface ApplicationFactory {

    /**
     * 创建Application
     * @return Application
     */
    Application createApplication();
}
