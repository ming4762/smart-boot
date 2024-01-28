package com.smart.commons.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;


/**
 * IP地址
 * @author jackson
 */
@Slf4j
public class IpUtils {

	private static final String UNKNOWN = "unknown";

	private static final String LOCALHOST_IP = "127.0.0.1";
	/**
	 * 客户端与服务器同为一台机器，获取的 ip 有时候是 ipv6 格式
	 */
	private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
	private static final String SEPARATOR = ",";

	private IpUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * 获取IP地址
	 * <p>
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddress() {
		return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()))
				.map(ServletRequestAttributes::getRequest)
				.map(IpUtils::getIpAddr)
				.orElse("");
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = null;
		try {
			ip = request.getHeader("x-forwarded-for");
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
				if (LOCALHOST_IP.equalsIgnoreCase(ip) || LOCALHOST_IPV6.equalsIgnoreCase(ip)) {
					// 根据网卡取本机配置的 IP
					InetAddress iNet = null;
					try {
						iNet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						log.error(e.getMessage(), e);
					}
					if (iNet != null) {
						ip = iNet.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			log.error("IPUtils ERROR ", e);
		}
		// 对于通过多个代理的情况，分割出第一个 IP
		if (ip != null && ip.length() > 15) {
			if (ip.contains(SEPARATOR)) {
				ip = ip.substring(0, ip.indexOf(SEPARATOR));
			}
		}
		return LOCALHOST_IPV6.equals(ip) ? LOCALHOST_IP : ip;
	}

}
