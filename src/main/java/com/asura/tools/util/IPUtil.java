package com.asura.tools.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IPUtil {
	public static String getLocalIp() {
		List<String> ips = new ArrayList<String>();
		NetworkInterface inter;
		try {
			Enumeration e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				inter = (NetworkInterface) e.nextElement();
				Enumeration en = inter.getInetAddresses();
				while (en.hasMoreElements()) {
					InetAddress dress = (InetAddress) en.nextElement();
					String ip = dress.getHostAddress();
					if (StringUtil.isNumberString(ip))
						ips.add(ip);
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		if (ips.size() > 0) {
			for (String ip : ips) {
				if ((!(ip.equals("127.0.0.1"))) && (!(ip.startsWith("192.168")))) {
					return ip;
				}
			}

			for (String ip : ips) {
				if (!(ip.equals("127.0.0.1"))) {
					return ip;
				}
			}
		}

		return "";
	}
}
