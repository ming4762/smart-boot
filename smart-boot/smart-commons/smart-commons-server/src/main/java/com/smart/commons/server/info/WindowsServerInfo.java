package com.smart.commons.server.info;

import lombok.SneakyThrows;

import java.util.Scanner;

/**
 * 获取服务器信息-window
 * @author shizhongming
 * 2022/12/4 16:42
 */
public class WindowsServerInfo extends AbstractServerInfo {

    /**
     * 获取CPU序列号
     *
     * @return cpu序列号
     */
    @Override
    public String getCpuSerial() {
        String[] shell = {"wmic cpu get processorid"};
        return this.exec(shell);
    }

    /**
     * 获取主板序列号
     *
     * @return 主板序列号
     */
    @Override
    public String getMainBoardSerial() {
        String[] shell = {"wmic baseboard get serialnumber"};
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
        Process process = Runtime.getRuntime().exec(command[0]);
        process.getOutputStream().close();

        try (Scanner scanner = new Scanner(process.getInputStream())) {
            if (scanner.hasNext()) {
                scanner.next();
            }
            if (scanner.hasNext()) {
                return scanner.next().trim();
            }
        }
        return null;
    }
}
