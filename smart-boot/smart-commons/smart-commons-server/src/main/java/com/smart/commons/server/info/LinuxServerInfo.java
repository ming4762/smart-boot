package com.smart.commons.server.info;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 获取服务器信息-linux
 * @author shizhongming
 * 2022/12/4 15:39
 */
public class LinuxServerInfo extends AbstractServerInfo {


    /**
     * 获取CPU序列号
     *
     * @return cpu序列号
     */
    @Override
    public String getCpuSerial() {
        String[] shell = {"/bin/bash","-c","dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"};
        return this.exec(shell);
    }

    /**
     * 获取主板序列号
     *
     * @return 主板序列号
     */
    @Override
    public String getMainBoardSerial() {
        String[] shell = {"/bin/bash","-c","dmidecode | grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1"};
        return this.exec(shell);
    }


    /**
     * 执行脚本
     *
     * @param command 命令
     * @return 执行结果
     */
    @SneakyThrows
    @Override
    public String exec(String[] command) {
        Process process = Runtime.getRuntime().exec(command);
        process.getOutputStream().close();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine().trim();
            if (!"".equals(line.trim())) {
                return line;
            }
        }
        return null;
    }
}
