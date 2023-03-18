package com.smart.commons.server.info;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2022/12/4 15:39
 */
@Slf4j
public abstract class AbstractServerInfo implements ServerInfo {

    /**
     * 获取mac地址
     *
     * @return mac地址
     */
    @Override
    public List<String> getMacAddress() {
        return this.getLocalInetAddress().stream()
                .map(this::getMacByInetAddress)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 获取IP地址
     *
     * @return IP地址
     */
    @Override
    public List<String> getIpAddress() {
        return this.getLocalInetAddress().stream()
                .map(InetAddress::getHostAddress)
                .distinct()
                .toList();
    }

    /**
     * 获取服务器所有的InetAddress
     * @return InetAddress 列表
     */
    @SneakyThrows(SocketException.class)
    protected List<InetAddress> getLocalInetAddress() {
        List<InetAddress> inetAddressList = new ArrayList<>(4);

        Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaceEnumeration.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
            Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
            while (inetAddressEnumeration.hasMoreElements()) {
                InetAddress inetAddress = inetAddressEnumeration.nextElement();
                if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.isMulticastAddress()) {
                    inetAddressList.add(inetAddress);
                }
            }
        }

        return inetAddressList;
    }

    /**
     * 获取网络接口的mac地址
     * @param inetAddress 网络接口
     * @return mac地址
     */
    protected String getMacByInetAddress(InetAddress inetAddress) {
        try {
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0; i<mac.length; i++) {
                if (i != 0) {
                    stringBuilder.append("-");
                }
                String temp = Integer.toHexString(mac[i] & 0xff);
                if(temp.length() == 1){
                    stringBuilder.append("0");
                }
                stringBuilder.append(temp);
            }
            return stringBuilder.toString().toUpperCase();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

}
