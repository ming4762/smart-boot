package com.smart.commons.server.info;

import java.util.List;

/**
 * 获取服务器信息
 * @author shizhongming
 * 2022/12/4 15:32
 */
public interface ServerInfo {

    /**
     * 获取mac地址
     * @return mac地址
     */
    List<String> getMacAddress();

    /**
     * 获取IP地址
     * @return IP地址
     */
    List<String> getIpAddress();

    /**
     * 获取CPU序列号
     * @return cpu序列号
     */
    String getCpuSerial();

    /**
     * 获取主板序列号
     * @return 主板序列号
     */
    String getMainBoardSerial();

    /**
     * 执行脚本
     * @param command 命令
     * @return 执行结果
     */
    String exec(String[] command);
}
